/*
 * This file is generated by jOOQ.
 */
package eu.fancybrackets.template.jooq.generated;


import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;


/**
 * Convenience access to all sequences in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>public.measurement_id_seq</code>
     */
    public static final Sequence<Integer> MEASUREMENT_ID_SEQ = new SequenceImpl<Integer>("measurement_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.INTEGER.nullable(false));
}