package com.github.andx2.storage.DAO;

import com.github.andx2.storage.pojo.TrustMessage;
import com.github.andx2.utils.Log;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Created by savos on 30.10.2016.
 */
public class TrustMessageDAOImpl implements TrustMessageDAO {

    private EntityManager em;

    public TrustMessageDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public TrustMessage putMessage(TrustMessage message) {
        try {
            em.getTransaction().begin();
            em.persist(message);
        } finally {
            em.getTransaction().commit();
        }
        return message;
    }

    @Override
    public TrustMessage getMessageByToken(String token) {
        TypedQuery<TrustMessage> query =
                em.createQuery("SELECT p FROM TrustMessage p WHERE trustToken = '" + token + "'", TrustMessage.class);
        TrustMessage result = null;
        try {
            result = query.getSingleResult();
        } catch (Exception e) {
            Log.i("TrustMessageDAOImpl.getMessageByToken message not found");
        }
        return result;
    }

    @Override
    public TrustMessage deleteMessageByToken(String token) {
        TrustMessage message;
        try {
            em.getTransaction().begin();
            message = getMessageByToken(token);
            em.remove(message);
        } finally {
            em.getTransaction().commit();
        }
        return message;
    }
}
