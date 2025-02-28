package com.ies.ApplicationRegistrationMS.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import java.time.LocalDateTime;


public class CaseNumberGenerator extends SequenceStyleGenerator {

    private static int counter = 1001;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        int id = counter++;
        LocalDateTime now = LocalDateTime.now();
        String value = "IES-CASE-" + now.getDayOfMonth() + now.getMonthValue() + now.getYear() + now.getHour()
                + now.getMinute();
        return value + id;
    }

}

