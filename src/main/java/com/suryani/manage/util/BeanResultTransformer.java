package com.suryani.manage.util;

import org.hibernate.HibernateException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import com.suryani.manage.db.DBUtil;

public class BeanResultTransformer extends AliasedTupleSubsetResultTransformer {
    private static final long serialVersionUID = 4398732142808577936L;
    private final Class<?> resultClass;
    private boolean isInitialized;
    private Setter[] setters;

    public BeanResultTransformer(Class<?> resultClass) {
        if (resultClass == null) {
            throw new IllegalArgumentException("resultClass cannot be null");
        }
        isInitialized = false;
        this.resultClass = resultClass;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;

        try {
            if (!isInitialized) {
                initialize(aliases);
            }

            result = resultClass.newInstance();

            for (int i = 0; i < aliases.length; i++) {
                if (setters[i] != null) {
                    setters[i].set(result, tuple[i], null);
                }
            }
        } catch (InstantiationException e) {
            throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
        } catch (IllegalAccessException e) {
            throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
        }

        return result;
    }

    private void initialize(String[] aliases) {
        PropertyAccessor propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[] { PropertyAccessorFactory.getPropertyAccessor(resultClass, null),
                PropertyAccessorFactory.getPropertyAccessor("field") });
        setters = new Setter[aliases.length];
        for (int i = 0; i < aliases.length; i++) {
            String alias = DBUtil.toBeanField(aliases[i]);
            if (alias != null) {
                setters[i] = propertyAccessor.getSetter(resultClass, alias);
            }
        }
        isInitialized = true;
    }

}
