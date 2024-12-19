package com.exceeddata.ac.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.exceeddata.ac.common.data.type.Types;
import com.exceeddata.ac.common.data.typedata.DataConv;
import com.exceeddata.ac.common.data.typedata.MapData;
import com.exceeddata.ac.common.data.typedata.NullData;
import com.exceeddata.ac.common.data.typedata.StringData;
import com.exceeddata.ac.common.data.typedata.TypeData;
import com.exceeddata.ac.common.exception.EngineException;

public class FileOperationUtils {

    private FileOperationUtils() {}
    
    /**
     * Read a hadoop file to string, and on fallback if hadoop lib is not available, read a local file to string.
     * 
     * @param path the path to the file
     * @param nullIfNotExists whether to return null if file not exists; if false then exception will be thrown.
     * @return String
     * @throws EngineException throws EngineException if problems occur
     */
    public static String readFile (
            final String path, 
            final boolean nullIfNotExists) throws EngineException {
        final Object[] args = new Object[] {path, nullIfNotExists};
        final Class<?>[] clazs = new Class<?>[] {String.class, Boolean.class};
        boolean reflectionError = false;
        Throwable retainedException = null;
        
        try {
            return (String) XReflectiveUtils.invokeStaticMethod("com.exceeddata.ac.hadoop.core.util.HadoopUtils", "readFile", args, clazs);
        } catch (NoClassDefFoundError | ClassNotFoundException | NoSuchMethodException e) {
            reflectionError = true;
            retainedException = e;
        } catch (InvocationTargetException | RuntimeException e) {
            retainedException = e;
        }
        
        //try local files
        final StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            final File f = new File(path);
            if (!f.exists()) {
                if (nullIfNotExists) {
                    return null;
                }
                if (reflectionError == false) {
                    throw new IOException ("FILE_MISSING: " + path);
                }
                throw new IOException("FILE_MIISING_OR_ERROR: " + retainedException.getMessage(), retainedException);
            } else if (f.isDirectory()) {
                throw new IOException("FILE_IS_FOLDER: " + path);
            }
            
            br = new BufferedReader(new FileReader(path));
            String line;
        
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (SecurityException | IOException e) {
            throw new EngineException (e.getMessage(), e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex){}
        }
    }

    /**
     * Write to a hadoop file, and on fallback if hadoop lib is not available, write to local file.
     * 
     * @param path the path to the file
     * @param object the object to write
     * @throws EngineException if an exception occurs
     */
    public static void writeFile (
            final String path,
            final Object object) throws EngineException {
        final Object[] args = new Object[]{path, object};
        final Class<?>[] clazs = new Class<?>[]{String.class, Object.class};
        boolean reflectionError = false;

        try {
            XReflectiveUtils.invokeStaticMethod("com.exceeddata.ac.hadoop.core.util.HadoopUtils", "writeFile", args, clazs);
            return;
        } catch (NoClassDefFoundError | ClassNotFoundException | NoSuchMethodException e) {
            reflectionError = true;
        } catch (InvocationTargetException | RuntimeException e) {
        }

        //try local files
        BufferedWriter bw = null;
        try {
            if (!reflectionError) {
                throw new IOException ("FILE_MISSING: " + path);
            }
            bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(path))));
            bw.write(object.toString());
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }



    /**
     * Read the file to string list.
     * 
     * @param path the path to the file
     * @param nullIfNotExists whether to return null if file not exists; if false then exception will be thrown.
     * @return String List
     * @throws EngineException throws EngineException if problems occur
     */
    @SuppressWarnings("unchecked")
    public static List<String> readFileToList (
            final String path, 
            final boolean nullIfNotExists) throws EngineException {
        final Object[] args = new Object[] {path, nullIfNotExists};
        final Class<?>[] clazs = new Class<?>[] {String.class, Boolean.class};
        boolean reflectionError = false;
        Throwable retainedException = null;
        
        try {
            return (List<String>) XReflectiveUtils.invokeStaticMethod("com.exceeddata.ac.hadoop.core.util.HadoopUtils", "readFileToList", args, clazs);
        } catch (NoClassDefFoundError | ClassNotFoundException | NoSuchMethodException e) {
            reflectionError = true;
            retainedException = e;
        } catch (InvocationTargetException | RuntimeException e) {
            retainedException = e;
        }
        //try local files
        final ArrayList<String> list = new ArrayList<String>();
        BufferedReader br = null;
        try {
            final File f = new File(path);
            if (!f.exists()) {
                if (nullIfNotExists) {
                    return null;
                }
                if (reflectionError == false) {
                    throw new IOException ("FILE_MISSING: " + path);
                }
                throw new IOException("FILE_MIISING_OR_ERROR: " + retainedException.getMessage(), retainedException);
            } else if (f.isDirectory()) {
                throw new IOException("FILE_IS_FOLDER: " + path);
            }
            
            br = new BufferedReader(new FileReader(path));
            String line;
        
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            return list;
        } catch (SecurityException | IOException e) {
            throw new EngineException (e.getMessage(), e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex){}
        }
    }
    
    /**
     * Read the file to XML Document.
     * 
     * @param path the path to the file
     * @param useNamespace whether to use namespace
     * @param nullIfNotExists whether to return null if file not exists; if false then exception will be thrown.
     * @return XML Document
     * @throws EngineException throws EngineException if problems occur
     */
    public static Document readFileToDocument (
            final String path, 
            final boolean useNamespace,
            final boolean nullIfNotExists) throws EngineException {
        final Object[] args = new Object[] {path, nullIfNotExists};
        final Class<?>[] clazs = new Class<?>[] {String.class, Boolean.class};
        boolean reflectionError = false;
        Throwable retainedException = null;
        
        try {
            return (Document) XReflectiveUtils.invokeStaticMethod("com.exceeddata.ac.hadoop.core.util.HadoopDocumentUtils", "readFileToDocument", args, clazs);
        }  catch (NoClassDefFoundError | ClassNotFoundException | NoSuchMethodException e) {
            reflectionError = true;
            retainedException = e;
        } catch (InvocationTargetException | RuntimeException e) {
            retainedException = e;
        }
        //try local files
        try {
            final File f = new File(path);
            if (!f.exists()) {
                if (nullIfNotExists) {
                    return null;
                }
                if (reflectionError == false) {
                    throw new IOException ("FILE_MISSING: " + path);
                }
                throw new IOException("FILE_MIISING_OR_ERROR: " + retainedException.getMessage(), retainedException);
            } else if (f.isDirectory()) {
                throw new IOException("FILE_IS_FOLDER: " + path);
            }
            
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(useNamespace);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(path);
        } catch (SecurityException | IOException e) {
            throw new EngineException (e.getMessage(), e);
        } catch (SAXException | ParserConfigurationException ex) {
            throw new EngineException (ex);
        }
    }
    
    /**
     * Read the file to string with line break.
     * 
     * @param path the path to the file
     * @param nullIfNotExists whether to return null if file not exists; if false then exception will be thrown.
     * @return String
     * @throws EngineException throws EngineException if problems occur
     */
    public static String readFileWithLR (
            final String path, 
            final boolean nullIfNotExists) throws EngineException {
        final Object[] args = new Object[] {path, nullIfNotExists};
        final Class<?>[] clazs = new Class<?>[] {String.class, Boolean.class};
        boolean reflectionError = false;
        Throwable retainedException = null;
        
        try {
            return (String) XReflectiveUtils.invokeStaticMethod("com.exceeddata.ac.hadoop.core.util.HadoopUtils", "readFileWithLR", args, clazs);
        }  catch (NoClassDefFoundError | ClassNotFoundException | NoSuchMethodException e) {
            reflectionError = true;
            retainedException = e;
        } catch (InvocationTargetException | RuntimeException e) {
            retainedException = e;
        }
        //try local files
        BufferedReader br = null;
        try {
            final File f = new File(path);
            if (!f.exists()) {
                if (nullIfNotExists) {
                    return null;
                }
                if (reflectionError == false) {
                    throw new IOException ("FILE_MISSING: " + path);
                }
                throw new IOException("FILE_MIISING_OR_ERROR: " + retainedException.getMessage(), retainedException);
            } else if (f.isDirectory()) {
                throw new IOException("FILE_IS_FOLDER: " + path);
            }
            
            final StringBuilder sb = new StringBuilder();
            br = new BufferedReader(new FileReader(path));
            String line;
        
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (SecurityException | IOException e) {
            throw new EngineException (e.getMessage(), e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex){}
        }
    }
    
    /**
     * Read the file to bytes.
     * 
     * @param path the path to the file
     * @param nullIfNotExists whether to return null if file not exists; if false then exception will be thrown.
     * @return bytes
     * @throws EngineException throws EngineException if problems occur
     */
    public static byte[] readFileToBytes (
            final String path, 
            final boolean nullIfNotExists) throws EngineException {
        final Object[] args = new Object[] {path, nullIfNotExists};
        final Class<?>[] clazs = new Class<?>[] {String.class, Boolean.class};
        boolean reflectionError = false;
        Throwable retainedException = null;
        
        try {
            return (byte[]) XReflectiveUtils.invokeStaticMethod("com.exceeddata.ac.hadoop.core.util.HadoopUtils", "readFileToBytes", args, clazs);
        }  catch (NoClassDefFoundError | ClassNotFoundException | NoSuchMethodException e) {
            reflectionError = true;
            retainedException = e;
        } catch (InvocationTargetException | RuntimeException e) {
            retainedException = e;
        }
        //try local files
        BufferedInputStream in = null;
        byte[] data = null;
        try {
            final File f = new File(path);
            if (!f.exists()) {
                if (nullIfNotExists) {
                    return null;
                }
                if (reflectionError == false) {
                    throw new IOException ("FILE_MISSING: " + path);
                }
                throw new IOException("FILE_MIISING_OR_ERROR: " + retainedException.getMessage(), retainedException);
            } else if (f.isDirectory()) {
                throw new IOException("FILE_IS_FOLDER: " + path);
            }
            
            in = new BufferedInputStream(new FileInputStream(path));
            
            final byte[] bytes = new byte[16384];
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int nRead;
            
            while ((nRead = in.read(bytes, 0, bytes.length)) != -1) {
                outputStream.write(bytes, 0, nRead);
            }

            outputStream.flush();
            
            data = outputStream.toByteArray();
            outputStream.close();
        } catch (SecurityException | IOException e) {
            throw new EngineException (e.getMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex){}
        }
        
        return data;
    }
    
    /**
     * Read the files into list
     * 
     * @param type the to-be-read type
     * @param path the file path
     * @return MutableGroupData
     * @throws EngineException if exception occurs
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<TypeData> readFiles2GroupData(
            final byte type,
            final String path) throws EngineException {
        final Object[] args = new Object[] {type, path};
        final Class<?>[] clazs = new Class<?>[] {Byte.class, String.class};
        boolean reflectionError = false;
        Throwable retainedException = null;
        
        try {
            return (ArrayList<TypeData>) XReflectiveUtils.invokeStaticMethod("com.exceeddata.ac.hadoop.core.util.HadoopUtils", "readFiles2GroupData", args, clazs);
        }  catch (NoClassDefFoundError | ClassNotFoundException | NoSuchMethodException e) {
            reflectionError = true;
            retainedException = e;
        } catch (InvocationTargetException | RuntimeException e) {
            retainedException = e;
        }
        
        try {
            final ArrayList<TypeData> group = new ArrayList<>();
            final File f = new File(path);
            if (!f.exists()) {
                if (reflectionError == false) {
                    throw new IOException ("FILE_MISSING: " + path);
                }
                throw new IOException("FILE_MIISING_OR_ERROR: " + retainedException.getMessage(), retainedException);
            }
            
            if (f.isDirectory()) {
                for (final File fileEntry : f.listFiles()) {
                    readFiles2GroupDataInternal(group, type, fileEntry);
                }
            } else {
                readFile2ListDataInternal(group, type, f);
            
            }
            return group;
        } catch (SecurityException | IOException e) {
            throw new EngineException (e.getMessage(), e);
        }
    }
    
    private static void readFiles2GroupDataInternal(
            final ArrayList<TypeData> group,
            final byte type,
            final File file) throws IOException, EngineException {
        if (file.isDirectory()) {
            for (final File fileEntry : file.listFiles()) {
                readFiles2GroupDataInternal(group, type, fileEntry);
            }
        } else {
            readFile2ListDataInternal(group, type, file);
        }
    }
    
    private static void readFile2ListDataInternal(
            final ArrayList<TypeData> group,
            final byte type,
            final File file) throws IOException, EngineException {
        final SeparatorParser parser = new SeparatorParser();
        BufferedReader br = null;
        try {
            br=new BufferedReader(new FileReader(file));
            String line = br.readLine();
            String[] parsed;
    
            if (type == Types.STRING) {
                while (line != null) {
                    parsed = parser.split(line);
                    if (parsed.length > 0) {
                        group.add(StringData.valueOf(parsed[0]));
                    }
                    line = br.readLine();
                }
            } else if (type == Types.NULL) {
                while (line != null) {
                    group.add(NullData.INSTANCE);
                    line = br.readLine();
                }
            } else {
                while (line != null) {
                    parsed = parser.split(line);
                    if (parsed.length > 0) {
                        group.add(DataConv.convert(StringData.valueOf(parsed[0]), type)); 
                    }
                    line = br.readLine();
                }
            }
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex){}
            }
        }
    }

    /**
     * Read the files into MapData
     * 
     * @param keyType the to-be-read key type
     * @param valueType the to-be-read value type
     * @param path the file path
     * @return MutableMapData
     * @throws EngineException if exception occurs
     */
    public static MapData readFiles2MapData(
            final byte keyType,
            final byte valueType,
            final String path) throws EngineException {
        final Object[] args = new Object[] {keyType, valueType, path};
        final Class<?>[] clazs = new Class<?>[] {Integer.class, Integer.class, String.class};
        boolean reflectionError = false;
        Throwable retainedException = null;
        
        try {
            return (MapData) XReflectiveUtils.invokeStaticMethod("com.exceeddata.ac.hadoop.core.util.HadoopUtils", "readFiles2MapData", args, clazs);
        }  catch (NoClassDefFoundError | ClassNotFoundException | NoSuchMethodException e) {
            reflectionError = true;
            retainedException = e;
        } catch (InvocationTargetException | RuntimeException e) {
            retainedException = e;
        }
        try {
            final File f = new File(path);
            if (!f.exists()) {
                if (reflectionError == false) {
                    throw new IOException ("FILE_MISSING: " + path);
                }
                throw new IOException("FILE_MIISING_OR_ERROR: " + retainedException.getMessage(), retainedException);
            }
            
            final LinkedHashMap<TypeData, TypeData> maps = new LinkedHashMap<>();
            if (f.isDirectory()) {
                for (final File fileEntry : f.listFiles()) {
                    readFiles2MapDataInternal(maps, keyType, valueType, fileEntry);
                }
            } else {
                readFile2MapDataInternal(maps, keyType, valueType, f);
            
            }
            return new MapData(maps);
        } catch (SecurityException | IOException e) {
            throw new EngineException (e.getMessage(), e);
        }
    }
    
    private static void readFiles2MapDataInternal(
            final LinkedHashMap<TypeData, TypeData> maps,
            final byte keyType,
            final byte valueType,
            final File file) throws IOException, EngineException {
        if (file.isDirectory()) {
            for (final File fileEntry : file.listFiles()) {
                readFiles2MapDataInternal(maps, keyType, valueType, fileEntry);
            }
        } else {
            readFile2MapDataInternal(maps, keyType, valueType, file);
        }
    }
    
    private static void readFile2MapDataInternal(
            final LinkedHashMap<TypeData, TypeData> maps,
            final byte keyType,
            final byte valueType,
            final File file) throws IOException, EngineException {
        final SeparatorParser parser = new SeparatorParser();
        BufferedReader br = null;
        try {
            br=new BufferedReader(new FileReader(file));
            String line = br.readLine();
            String[] parsed;
    
            if (keyType == Types.STRING && valueType == Types.STRING) {
                while (line != null) {
                    parsed = parser.split(line);
                    if (parsed.length > 0) {
                        if (parsed.length == 1) {
                            maps.put(StringData.valueOf(parsed[0]), StringData.NULL);
                        } else {
                            maps.put(StringData.valueOf(parsed[0]), StringData.valueOf(parsed[1]));
                        }
                    }
                    line = br.readLine();
                }
            } else {
                while (line != null) {
                    parsed = parser.split(line);
                    if (parsed.length > 0) {
                        if (parsed.length == 1) {
                            maps.put(
                                    DataConv.convert(StringData.valueOf(parsed[0]), keyType),
                                    DataConv.convert(StringData.NULL, valueType));
                        } else {
                            maps.put(
                                    DataConv.convert(StringData.valueOf(parsed[0]), keyType),
                                    DataConv.convert(StringData.valueOf(parsed[1]), valueType));
                        }
                    }
                    line = br.readLine();
                }
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch(IOException ex) {}
        }
    }
}
