package com.beust.doclipse.doclet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.generama.ConfigurableDocletTagFactory;

import com.thoughtworks.qdox.model.AbstractBaseJavaEntity;
import com.thoughtworks.qdox.model.DefaultDocletTag;
import com.thoughtworks.qdox.model.DocletTag;

public class DocletTagFactoryImpl  extends ConfigurableDocletTagFactory{
	private List unknownTags = new ArrayList();
    private final Map registeredTags = new HashMap();

    public DocletTagFactoryImpl() {
        String[] defaultTags = new String[]{"param", "author", "see", "since", "exception", "throws", "version", "return", "inheritDoc", "deprecated"};
        for (int i = 0; i < defaultTags.length; i++) {
            registerTag(defaultTags[i], DefaultXDocletTag.class);
        }
    }

    public DocletTag createDocletTag(String tag, String text) {
        throw new UnsupportedOperationException();
    }

    public DocletTag createDocletTag(String tag, String text, AbstractBaseJavaEntity context, int lineNumber) {
        Class tagClass = (Class) registeredTags.get(tag);

        boolean isKnown = true;
        if (tagClass == null) {
            tagClass = DefaultXDocletTag.class;
            isKnown = false;
        }
        try {
            Constructor newTag = tagClass.getConstructor(new Class[]{String.class, String.class, AbstractBaseJavaEntity.class, Integer.TYPE});
            DocletTag result = (DocletTag) newTag.newInstance(new Object[]{tag, text, context, new Integer(lineNumber)});

            if (!isKnown) {
                unknownTags.add(result);
            }
            return result;
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No (String, String, AbstractBaseJavaEntity, int) constructor in " + tagClass.getName());
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }

    public void registerTag(String tagName, Class tagClass) {
        registeredTags.put(tagName, tagClass != null ? tagClass : DefaultDocletTag.class);
    }

    public void registerTags(Map tags) {
        Set tagNames = tags.keySet();
        for (Iterator iterator = tagNames.iterator(); iterator.hasNext();) {
            String tagName = (String) iterator.next();
            registerTag(tagName, (Class) tags.get(tagName));
        }
    }

    public List getUnknownTags() {
        return unknownTags;
    }

    public void printUnknownTags() {
        for (Iterator iterator = unknownTags.iterator(); iterator.hasNext();) {
            DocletTag docletTag = (DocletTag) iterator.next();
            System.out.println("Unknown tag: @" + docletTag.getName() + " in " + ConfigurableDocletTagFactory.getLocation(docletTag) + " (line " + docletTag.getLineNumber() + ")");
        }
    }

}
