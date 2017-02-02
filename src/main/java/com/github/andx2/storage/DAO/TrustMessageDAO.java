package com.github.andx2.storage.DAO;

import com.github.andx2.storage.pojo.TrustMessage;

/**
 * Created by savos on 30.10.2016.
 */
public interface TrustMessageDAO {

    TrustMessage putMessage(TrustMessage message);

    TrustMessage getMessageByToken(String token);

    TrustMessage deleteMessageByToken(String token);
}
