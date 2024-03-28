package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;

public class JPA {
    public static void main(String[] args) throws SQLException {

        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
//            try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
//              Statement st = connection.createStatement()){
//                st.executeQuery("select id, name from person");
//            }
            insertPersons(sessionFactory);

            try (Session session = sessionFactory.openSession()) {
                Person person = session.find(Person.class, 1L);
                System.out.println(person);

                Transaction tx = session.beginTransaction();
                person.setName("new name");
                session.merge(person);
                tx.commit();
            }
            try (Session session = sessionFactory.openSession()) {
                Query<Person> query = session.createQuery("select p from Person p where id > :id", Person.class);
                query.setParameter("id", 5);
                List<Person> resultList = query.getResultList();
                Transaction tx = session.beginTransaction();
                for (Person person : resultList) {
                    person.setName("UPDATED");
                    session.merge(person);
                }
                tx.commit();
                System.out.println(session.createQuery("from Person p", Person.class).getResultList());

            }


            try (Session session = sessionFactory.openSession()) {
                Person person = session.find(Person.class, 1L);
                System.out.println(person);

                Transaction tx = session.beginTransaction();
                session.remove(person);
                tx.commit();
            }
            try (Session session = sessionFactory.openSession()) {
                Person person = session.find(Person.class, 1L);
                System.out.println(person);
            }





        }
    }

    private static void insertNewPerson(SessionFactory sessionFactory) {
        Person person = new Person();
        person.setId(1L);
        person.setName("Valya");


        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(person);
            tx.commit();
        }
//            try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
//                 Statement st = connection.createStatement()) {
//                ResultSet resultSet = st.executeQuery("select id, name from persons");  проверка вставки
//                while(resultSet.next()) {
//                    System.out.println("ID = " + resultSet.getInt("id"));
//                    System.out.println("NAME = " + resultSet.getString("name"));
//                }
//            }
    }
    private static void insertPersons(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Department department = new Department();
            department.setId(555L);
            department.setName("DEPARTMENT NAME");
            session.persist(department);

            for (long i = 1; i <= 10; i++) {
                Person person = new Person();
                person.setId(i);
                person.setName("Person #" + i);
                person.setDepartment(department);
                session.persist(person);
            }

            tx.commit();
        }
    }
}