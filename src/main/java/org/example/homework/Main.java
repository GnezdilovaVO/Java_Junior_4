package org.example.homework;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration().configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
           insertStudents(sessionFactory);

            try (Session session = sessionFactory.openSession()) {
                Student student = session.find(Student.class, 2L);
                System.out.println(student);
            }

            try (Session session = sessionFactory.openSession()) {
                Student student = session.find(Student.class, 5L);

                Transaction transaction = session.beginTransaction();
                session.remove(student);
                transaction.commit();
            }
            try (Session session = sessionFactory.openSession()) {
                Student student = session.find(Student.class, 1L);
                Transaction transaction = session.beginTransaction();
                student.setAge(19);
                session.merge(student);
                transaction.commit();
                System.out.println(student);
            }
            try (Session session = sessionFactory.openSession()) {
                Query<Student> query = session.createQuery("select s from Student s where age > :age", Student.class);
                query.setParameter("age", 20);
                List<Student> result = query.getResultList();
                System.out.println(result);

            }
        }
    }

    private static void insertStudents(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            for (int i = 1; i <= 10; i++) {
                Student student = new Student();
                student.setId(i);
                student.setFirstName("Person " + i);
                student.setSecondName("Person " + (i + 10));
                student.setAge(ThreadLocalRandom.current().nextInt(18, 66));
                session.persist(student);
                System.out.println(student);

            }
            transaction.commit();

        }
    }
}
