package com.exceeddata.ac.common.data;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.junit.Test;

import com.exceeddata.ac.common.data.record.Record;
import com.exceeddata.ac.common.data.typedata.BinaryData;
import com.exceeddata.ac.common.data.typedata.BooleanData;
import com.exceeddata.ac.common.data.typedata.CalendarTimeData;
import com.exceeddata.ac.common.data.typedata.CalendarTimestampData;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.DateData;
import com.exceeddata.ac.common.data.typedata.DecimalData;
import com.exceeddata.ac.common.data.typedata.DoubleData;
import com.exceeddata.ac.common.data.typedata.FloatData;
import com.exceeddata.ac.common.data.typedata.InstantData;
import com.exceeddata.ac.common.data.typedata.IntData;
import com.exceeddata.ac.common.data.typedata.ListData;
import com.exceeddata.ac.common.data.typedata.LongData;
import com.exceeddata.ac.common.data.typedata.MapData;
import com.exceeddata.ac.common.data.typedata.NumericData;
import com.exceeddata.ac.common.data.typedata.StringData;
import com.exceeddata.ac.common.data.typedata.TimeData;
import com.exceeddata.ac.common.data.typedata.TimestampData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class RecordSerialization {
    private static final String location = System.getProperty("java.io.tmpdir") + "/record.ser";
    
    private String serializeExternalizable(final String loc, final Record record) throws IOException {
        final FileOutputStream fileOut = new FileOutputStream(loc);
        final ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
        outStream.writeObject(record);
        outStream.close();
        fileOut.close();
        return loc;
    }
    
    private Record deserializeExternalizable(final String loc) throws IOException, ClassNotFoundException {
        final FileInputStream fileIn = new FileInputStream(loc);
        final ObjectInputStream inStream = new ObjectInputStream(fileIn);
        final Record obj = (Record) inStream.readObject();
        inStream.close();
        fileIn.close();
        return obj;
    }
    
    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException, EngineException {
        final Record record = new Record();
        record.add("a1", BooleanData.NULL);
        record.add("a2", BooleanData.TRUE);
        record.add("a3", BooleanData.FALSE);
        record.add("a4", IntData.NULL);
        record.add("a5", IntData.ZERO);
        record.add("a6", IntData.valueOf(100));
        record.add("a7", IntData.valueOf(300));
        record.add("a8", IntData.valueOf(100000));
        record.add("a9", FloatData.valueOf(100f));
        record.add("a10", LongData.NULL);
        record.add("a11", BooleanData.NULL);
        record.add("a12", BooleanData.TRUE);
        record.add("a13", BooleanData.FALSE);
        record.add("a14", IntData.NULL);
        record.add("a15", IntData.ZERO);
        record.add("a16", IntData.valueOf(100));
        record.add("a17", IntData.valueOf(300));
        record.add("a18", IntData.valueOf(100000));
        record.add("a19", FloatData.valueOf(100f));
        record.add("a20", LongData.NULL);
        record.add("a21", LongData.ZERO);
        record.add("a22", LongData.valueOf(100l));
        record.add("a23", LongData.valueOf(300l));
        record.add("a24", LongData.valueOf(100000l));
        record.add("a25", LongData.valueOf(10000000000l));
        record.add("a26", DoubleData.valueOf(100d));
        record.add("a27", NumericData.valueOf(100d));
        record.add("a28", DecimalData.valueOf(BigDecimal.valueOf(100l)));
        record.add("a29", DecimalData.valueOf(new BigDecimal("0")));
        record.add("a30", DecimalData.valueOf(new BigDecimal("0.13828")));
        record.add("a31", DecimalData.valueOf(new BigDecimal("13828939292")));
        record.add("a32", DecimalData.valueOf(BigDecimal.ONE.divide(BigDecimal.valueOf(3l), new MathContext(32, RoundingMode.HALF_UP))));
        record.add("a33", DecimalData.valueOf(new BigDecimal(new BigInteger("37107287533902102798797998220837590246510135740250"), 0)));
        record.add("a34", StringData.NULL);
        record.add("a35", StringData.EMPTY);
        record.add("a36", StringData.valueOf("1"));
        record.add("a37", StringData.valueOf("coolaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        record.add("a38", BinaryData.NULL);
        record.add("a39", BinaryData.EMPTY);
        record.add("a40", BinaryData.valueOf("cool"));
        record.add("a41", BinaryData.valueOf("coolaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        record.add("a42", DateData.valueOf("2017-10-15"));
        record.add("a43", TimeData.valueOf("11:20:31.281"));
        record.add("a44", TimestampData.valueOf("2017-10-15 11:20:31.281"));
        record.add("a45", CalendarTimeData.valueOf("11:20:31.281-0800"));
        record.add("a46", CalendarTimestampData.valueOf("2017-10-15 11:20:31.281-0800"));
        record.add("a47", InstantData.valueOf("2017-10-15 11:20:31.281"));
        
        final ListData list = new ListData(new ArrayList<>(Arrays.asList( new TypeData[] {DoubleData.valueOf(1d), DoubleData.valueOf(0d), DoubleData.valueOf(2d), DoubleData.valueOf(0d)})));
        final LinkedHashMap<TypeData, TypeData> md = new LinkedHashMap<>();
        md.put(StringData.valueOf("a"), DoubleData.valueOf(1d));
        md.put(StringData.valueOf("a"), IntData.valueOf(10));
        md.put(StringData.valueOf("a"), FloatData.valueOf(1f));
        md.put(StringData.valueOf("a"), NumericData.valueOf(1.1d));
        
        final MapData map = new MapData(md);
        record.add("a48", list);
        record.add("a49", DataConv.toSetData(list));
        record.add("a50", DataConv.toDenseVectorData(list));
        record.add("a51", DataConv.toSparseVectorData(list));
        record.add("a52", map);
        
        assertEquals(deserializeExternalizable(serializeExternalizable(location, record)), record);
    }
}
