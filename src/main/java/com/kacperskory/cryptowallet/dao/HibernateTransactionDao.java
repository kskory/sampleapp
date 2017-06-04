package com.kacperskory.cryptowallet.dao;

import com.kacperskory.cryptowallet.model.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.List;

@Repository
@Transactional
public class HibernateTransactionDao implements TransactionDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public List<Transaction> getAll() {
        return getSession().createQuery("from Transaction").list();
    }

    @Override
    public Transaction getById(long id) {
        return getSession().get(Transaction.class, id);
    }

    @Override
    public Long save(Transaction transaction) {
        return  (Long) getSession().save(transaction);
    }

    @Override
    public void deleteById(long id) {
        getSession().delete(getById(id));

    }
}