package ru.gb.lesson4;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.util.List;

public class JPA {
  public static void main(String[] args) {
    Configuration configuration = new Configuration().configure();

    try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
insertPersons(sessionFactory);

//Find
      try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();

        Student student = session.find(Student.class, 1L);
        System.out.println("\nПоиск студента по  id: " + student);

        tx.commit();
      }

//Persist
      try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();

        Student newStudent = new Student();
        newStudent.setFirstName("Aleksej");
        newStudent.setSecondName("VORONKIN");
        newStudent.setAge(25);
        session.persist(newStudent);
        System.out.println("\nНовый студент сохранен: " + newStudent);

        tx.commit();
      }

//Merge - менял имя
      try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();
        Student updatedStudent = session.find(Student.class, 1L);
        if (updatedStudent != null) {
          updatedStudent.setFirstName("Updated ");
          session.merge(updatedStudent);
          System.out.println("\nСтудент с ID " + updatedStudent.getId() + " updated: " + updatedStudent);
        } else {
          System.out.println("\nСтудент с указанным ID не найден.");
        }
        tx.commit();
      }
// Remove
      try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();
        Student studentToDelete = session.find(Student.class, 2L);
        if (studentToDelete != null) {
          session.remove(studentToDelete);
          System.out.println("\nСтудент с ID "+ studentToDelete.getId()+ " удален из БД.");
        } else {
          System.out.println("\nСтудент с указанным ID не найден.");
        }
        tx.commit();
      }

// Query
      try (Session session = sessionFactory.openSession()) {
        Query<Student> query = session.createQuery("SELECT s FROM Student s WHERE s.age > :age", Student.class);
        query.setParameter("age", 20);

        List<Student> olderStudents = query.getResultList();
        System.out.println("\nСтуденты страше 20 лет:");
        for (Student student : olderStudents) {
          System.out.println("\nStudent ID: " + student.getId() + ", Name: " + student.getFirstName() + " " + student.getSecondName() + ", Age: " + student.getAge());
        }
        System.out.println();
      }
    }
  }

  private static void insertPersons(SessionFactory sessionFactory) {
    try (Session session = sessionFactory.openSession()) {
      Transaction tx = session.beginTransaction();

      for (int i = 1; i <= 5; i++) {
        Student student = new Student();
        student.setFirstName("Test" + i);
        student.setSecondName("Student" + i);
        student.setAge(20 + i);
        session.persist(student);
      }

      tx.commit();
    }
  }

}
