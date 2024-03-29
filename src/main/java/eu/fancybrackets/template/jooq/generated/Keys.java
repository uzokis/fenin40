/*
 * This file is generated by jOOQ.
 */
package eu.fancybrackets.template.jooq.generated;


import eu.fancybrackets.template.jooq.generated.tables.Measurement;
import eu.fancybrackets.template.jooq.generated.tables.records.MeasurementRecord;

import javax.annotation.Generated;

import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<MeasurementRecord, Integer> IDENTITY_MEASUREMENT = Identities0.IDENTITY_MEASUREMENT;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<MeasurementRecord> MEASUREMENT_PKEY = UniqueKeys0.MEASUREMENT_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<MeasurementRecord, Integer> IDENTITY_MEASUREMENT = Internal.createIdentity(Measurement.MEASUREMENT, Measurement.MEASUREMENT.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<MeasurementRecord> MEASUREMENT_PKEY = Internal.createUniqueKey(Measurement.MEASUREMENT, "measurement_pkey", Measurement.MEASUREMENT.ID);
    }
}
