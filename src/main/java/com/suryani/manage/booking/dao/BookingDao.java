package com.suryani.manage.booking.dao;

import com.quidsi.core.util.StringUtils;
import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.db.BaseDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class BookingDao extends BaseDao<Booking> {
    public void updateStatus(String id, String status, String selectTime) {
        if (StringUtils.hasText(selectTime)) {
            em.createQuery("update Booking set status=?2,selectTime=?3 where id=?1").setParameter(1, id).setParameter(2, status).setParameter(3, selectTime).executeUpdate();
        } else {
            em.createQuery("update Booking set status=?2 where id=?1").setParameter(1, id).setParameter(2, status).executeUpdate();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Booking> findAll(Map<String, Object> params, int offset, int fetchSize) {
        StringBuffer hql = new StringBuffer(1280);
        hql.append("from Booking");
        if (!params.isEmpty()) {
            hql.append(" where ");
            for (Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> type = (Map.Entry<String, Object>) it.next();
                if ("beginTime".equals(type.getKey())) {
                    hql.append("selectDate").append(" >=:").append(type.getKey()).append(" AND ");
                } else if ("endTime".equals(type.getKey())) {
                    hql.append("selectDate").append(" <=:").append(type.getKey()).append(" AND ");
                } else {
                    hql.append(type.getKey()).append("=:").append(type.getKey()).append(" AND ");
                }
            }
            hql.replace(hql.length() - 4, hql.length(), "");
        }
        Query query = em.createQuery(hql + " order by selectDate desc");
        if (!params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    public int getTotalSize(Map<String, Object> params) {
        StringBuffer hql = new StringBuffer("select count(id) from Booking");
        if (!params.isEmpty()) {
            hql.append(" where ");
            for (Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> type = (Map.Entry<String, Object>) it.next();
                if ("beginTime".equals(type.getKey())) {
                    hql.append("selectDate").append(" >=:").append(type.getKey()).append(" AND ");
                } else if ("endTime".equals(type.getKey())) {
                    hql.append("selectDate").append(" <=:").append(type.getKey()).append(" AND ");
                } else {
                    hql.append(type.getKey()).append("=:").append(type.getKey()).append(" AND ");
                }
            }
            hql.replace(hql.length() - 4, hql.length(), "");
        }
        Query query = em.createQuery(hql.toString());
        if (!params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return ((Long) query.getSingleResult()).intValue();
    }

    public void delete(String id) {
        em.createQuery("delete from Booking where id=?1").setParameter(1, id).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<Booking> findAfterOneWeek() {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE) + 7);
        Query query = em.createQuery("from Booking where selectDate=?1 and status<>2");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            query.setParameter(1, sdf.parse(sdf.format(now.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return query.getResultList();
    }
}
