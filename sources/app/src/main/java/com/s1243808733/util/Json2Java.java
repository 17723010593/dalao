package com.s1243808733.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json2Java {

    public static String createBean(String json, Configurd configurd) throws JSONException {
        try {
            return createBean(new JSONObject(json), configurd);
        } catch (JSONException e) {
            try {
                return createBean(new JSONArray(json), configurd);
            } catch (JSONException e2) {}
        }
        throw new JSONException(json);
    }

    public static String createBean(JSONArray jSONArray, Configurd configurd) throws JSONException {
        if (jSONArray.length() > 0) {
            Object item0=jSONArray.get(0);
            if (item0 instanceof JSONObject) {
                JSONObject object=(JSONObject)item0;
                return createBean(object, configurd);   
            } else {
                throw new JSONException("不支持此无key类型数组转换");
                //return createClass(configurd.getSpace() + "//不支持此无key类型数组转换\n\n", configurd);   
            }
        }
        return createClass("", configurd);
    }

	public static String createBean(JSONObject jSONObject, Configurd configurd) throws JSONException {
        return createClass(createObject(jSONObject, null, 0, configurd), configurd);
    }

    private static String createClass(String javaBean, Configurd configurd) {
        StringBuffer sb=new StringBuffer();

        if (configurd.getPackageName() != null) {
            sb.append("package " + configurd.getPackageName() + ";");
            sb.append("\n\n");
        }

        if (configurd.getClassName() != null) {

            if (configurd.getImports().keySet().size() > 0) {
                for (String imports: configurd.getImports().keySet()) {
                    sb.append("import ");
                    sb.append(imports);
                    sb.append(";\n");
                }
                sb.append("\n");
            }

            sb.append("public class " + configurd.getClassName() + " {");
            sb.append("\n\n");
            sb.append(javaBean);
            sb.append("}");

            return sb.toString();
        } else {
            return configurd.getSpace() + javaBean.trim();
        }
    }

    private static String createObject(JSONObject jSONObject, String className, int depth, Configurd configurd) throws JSONException {

        String space=configurd.getSpace();
        String classFrontSpace = "";
        String fieldFrontSpace =space;
        for (int i = 0; i < depth; i++) {
            classFrontSpace += space;
            fieldFrontSpace += space;
        }

        List<Field> list_field=new ArrayList<>();
        List<String> list_class=new ArrayList<>();

        Iterator<?> it = jSONObject.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object object = jSONObject.get(key);

            if (object instanceof JSONArray) {
                JSONArray jSONArray=(JSONArray)object;
                if (jSONArray.length() > 0) {
                    Object item0=jSONArray.get(0);

                    Object field=item0;
                    for (int i=0;i < jSONArray.length();i++) {
                        if (!jSONArray.get(i).getClass().equals(item0.getClass())) {
                            field = Object.class;
                            break;
                        }
                    }
                    list_field.add(createField(jSONArray, field instanceof JSONObject
                                               ?toUpperCaseFirst(key)
                                               : getSimpleName(field instanceof Class ?field: field, configurd), key, configurd));

                    if (item0 instanceof JSONObject) {
                        JSONObject jo=(JSONObject)item0;
                        list_class.add(createObject(jo, toUpperCaseFirst(key), depth + 1, configurd));
                    }

                } else {
                    list_field.add(createField(jSONArray, Object.class.getSimpleName(), key, configurd));
                }
            } else if (object instanceof JSONObject) {
                JSONObject jo=(JSONObject)object;
                String fieldType = toUpperCaseFirst(key);
                list_field.add(createField(jo, fieldType, key, configurd));
                list_class.add(createObject(jo, fieldType, depth + 1, configurd));
            } else {
                list_field.add(createField(object, getSimpleName(object, configurd), key, configurd));
            }

        }

        StringBuffer sb=new StringBuffer();
        StringBuffer sb_field=new StringBuffer();
        StringBuffer sb_method=new StringBuffer();
        StringBuffer sb_class=new StringBuffer();

        List<String> list_setter=new ArrayList<>();
        List<String> list_getter=new ArrayList<>();

        for (int i=0;i < list_field.size();i++) {
            Field field=list_field.get(i);
            sb_field.append(fieldFrontSpace + field.toString());
            sb_field.append("\n");
            if (i + 1 < list_field.size()) {
                sb_field.append("\n");
            }

            if (configurd.isSetters()) {
                String fieldType=field.getType();
                if (configurd.isUseArray() && field.isArray()) {
                    fieldType = fieldType.substring(0, fieldType.length() - 2) + "...";
                }
                StringBuffer method_setter=new StringBuffer();
                method_setter.append(fieldFrontSpace + "public void set" + toUpperCaseFirst(field.getName()));
                method_setter.append("(" + fieldType + " " + field.getName() + ") {");
                method_setter.append("\n");
                method_setter.append(fieldFrontSpace + space + "this." + field.getName() + " = " + field.getName() + ";");
                method_setter.append("\n");
                method_setter.append(fieldFrontSpace + "}");
                list_setter.add(method_setter.toString());
            }

            if (configurd.isGetters()) {
                StringBuffer method_getter=new StringBuffer();
                String prefix="get";
                if (boolean.class.getSimpleName().equals(field.getType())) {
                    prefix = "is";
                }
                method_getter.append(fieldFrontSpace + "public " + field.getType() + " " + prefix + toUpperCaseFirst(field.getName()) + "() {");
                method_getter.append("\n");
                method_getter.append(fieldFrontSpace + space + "return " + field.getName() + ";");
                method_getter.append("\n");
                method_getter.append(fieldFrontSpace + "}");
                list_getter.add(method_getter.toString());
            }

        }

        if (configurd.isSortMethod()) {
            for (String setter:list_setter) {
                sb_method.append(setter);
                sb_method.append("\n\n");
            }
            for (String getter:list_getter) {
                sb_method.append(getter);
                sb_method.append("\n\n");
            }
        } else {
            if (configurd.isSetters() && configurd.isGetters()) {
                for (int i=0;i < list_setter.size();i++) {
                    sb_method.append(list_setter.get(i));
                    sb_method.append("\n\n");
                    sb_method.append(list_getter.get(i));
                    sb_method.append("\n\n");
                }
            } else {
                for (String setter:list_setter) {
                    sb_method.append(setter);    
                    sb_method.append("\n\n");
                }
                for (String getter:list_getter) {
                    sb_method.append(getter);
                    sb_method.append("\n\n");
                }
            }
        }

        for (String clazz:list_class) {
            sb_class.append(clazz);
            sb_class.append("\n");
        }

        sb.append(sb_field);
        sb.append("\n");
        sb.append(sb_method);
        sb.append(sb_class);

        StringBuffer result=new StringBuffer();

        String toString="";
        if (configurd.isToString()) {
            String toStringReturn="\"[\"+getClass().getCanonicalName()+\"]\"";
            for (int i=0;i < list_field.size();i++) {
                Field field=list_field.get(i);
                toStringReturn += "\n" + fieldFrontSpace + space + " + \"" + (i == 0 ?"": ",") + "\\" + "n" + field.getName() + "=\" + " + field.getName();
            }
            toString = fieldFrontSpace + "@Override"
			+ "\n" + fieldFrontSpace + "public String toString() {"
			+ "\n" + fieldFrontSpace + space + "return " + toStringReturn + ";"
			+ "\n" + fieldFrontSpace + "}\n\n";

        }

        if (depth > 0) {
            result.append(classFrontSpace + "public static class " + className + " {");
            result.append("\n\n");
            if (sb.toString().trim().length() != 0) {
                result.append(sb);
            }

            result.append(toString);
            result.append(classFrontSpace);
            result.append("}\n");
        } else {
            result.append(sb);
            result.append(toString);

        }

        return result.toString();
    }

    private static String toUpperCaseFirst(CharSequence cs) {
        char[] charArray=cs.toString().toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return String.valueOf(charArray);
    }

    private static String getSimpleName(Object object, Configurd config) {
        if (JSONObject.NULL == object) {
            return Object.class.getSimpleName();
        } 

        Class[] old={
            Boolean.class,
            Integer.class,
            Long.class,
            Double.class
        };

        Class[] news={   
            boolean.class,
            int.class,
            long.class,
            double.class
        };

        Class clazz=object instanceof Class ?(Class)object: object.getClass();

        if (config.isUseInteger() && Integer.class.equals(clazz)) {
            return clazz.getSimpleName();
        } else { 
            for (int i=0;i < old.length;i++) {
                if (old[i].equals(clazz)) {
                    return news[i].getSimpleName();
                }
            }
        }

        return clazz.getSimpleName();
    }

    private static Field createField(Object jSONObject, String type, String name, Configurd configurd) {
        String modifier=configurd.isPublicField() ?"public": "private";

        if (jSONObject instanceof JSONArray) {

            Json2Java.Field field=new Field();
            field.setModifier(modifier);
            field.setName(name);
            field.setArray(true);

            if (configurd.isUseArray()) {
                field.setType(type + "[]");
            } else {
                Class<?> impl=List.class;
                field.setType(impl.getSimpleName() + "<" + type + ">");
                configurd.addImport(impl.getCanonicalName());
            }

            return field;
        }

        return new Field(modifier, type, name);
    }

    private static class Field {

        private String modifier;

        private String type;

        private String name;

        private boolean array;

        public Field() {}

        public Field(String modifier, String type, String name) {
            this.modifier = modifier;
            this.type = type;
            this.name = name;
        }

        public Field setModifier(String modifier) {
            this.modifier = modifier;
            return this;
        }

        public String getModifier() {
            return modifier;
        }

        public Field setType(String type) {
            this.type = type;
            return this;
        }

        public String getType() {
            return type;
        }

        public Field setName(String name) {
            this.name = name;
            return this;
        }

        public String getName() {
            return name;
        }

        public Field setArray(boolean array) {
            this.array = array;
            return this;
        }

        public boolean isArray() {
            return array;
        }

        @Override
        public String toString() {
            return modifier + " " + type + " " + name + ";";
        }

    }

    public static class Configurd {

        private int indentation=4;

        private String packageName;

        private String className;

        private boolean setters=true;

        private boolean getters=true;

        private boolean sortMethod;

        private boolean publicField;

        private boolean useArray;

        private boolean useInteger;

        private boolean toString;

        private Map<String,Void> imports=new HashMap<>();

        public Configurd() {}

        public Configurd(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        public void setUseInteger(boolean useInteger) {
            this.useInteger = useInteger;
        }

        public boolean isUseInteger() {
            return useInteger;
        }

        public Configurd setIndentation(int indentation) {
            this.indentation = indentation;
            return this;
        }

        public int getIndentation() {
            return indentation;
        }

        public Configurd setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public String getPackageName() {
            return packageName;
        }

        public Configurd setClassName(String className) {
            this.className = className;
            return this;
        }

        public String getClassName() {
            return className;
        }

        public Configurd setSetters(boolean setters) {
            this.setters = setters;
            return this;
        }

        public boolean isSetters() {
            return setters;
        }

        public Configurd setGetters(boolean getters) {
            this.getters = getters;
            return this;
        }

        public boolean isGetters() {
            return getters;
        }

        public Configurd setSortMethod(boolean sortMethod) {
            this.sortMethod = sortMethod;
            return this;
        }

        public boolean isSortMethod() {
            return sortMethod;
        }

        public Configurd setPublicField(boolean publicField) {
            this.publicField = publicField;
            return this;
        }

        public boolean isPublicField() {
            return publicField;
        }

        public Configurd setUseArray(boolean useArray) {
            this.useArray = useArray;
            return this;
        }

        public boolean isUseArray() {
            return useArray;
        }

        public Configurd setToString(boolean toString) {
            this.toString = toString;
            return this;
        }

        public boolean isToString() {
            return toString;
        }

        public Map<String, Void> getImports() {
            return imports;
        }

        public Configurd addImport(String className) {
            getImports().put(className, null);
            return this;
        }

        public String getSpace() {
            StringBuffer sb=new StringBuffer();
            for (int i=0;i < getIndentation();i++) {
                sb.append(" ");
            }
            return sb.toString();
        }

    }

}

