package com.suryani.manage.system.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.suryani.manage.db.BaseDao;
import com.suryani.manage.system.domain.SystemLog;
import com.suryani.manage.system.dto.SystemLogDto;
import com.suryani.manage.util.BeanResultTransformer;

@Repository
public class SystemLogDao extends BaseDao<SystemLog> {

    @SuppressWarnings("unchecked")
    public List<SystemLog> list(int offset, int fetchSize) {
        Query query = em.createQuery("from SystemLog");
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<SystemLogDto> listDetail(int offset, int fetchSize) {
        Query query = em.createNativeQuery("select t.* , t1.name from system_log t , system_user t1 where t1.id=t.user_id");
        org.hibernate.Query hq = query.unwrap(org.hibernate.Query.class);
        hq.setResultTransformer(new BeanResultTransformer(SystemLogDto.class));
        hq.setFirstResult(offset);
        hq.setMaxResults(fetchSize);
        return hq.list();
    }

    public int getTotalSize() {
        Query query = em.createQuery("select count(id) from SystemLog ");
        return ((Long) query.getSingleResult()).intValue();
    }

}
