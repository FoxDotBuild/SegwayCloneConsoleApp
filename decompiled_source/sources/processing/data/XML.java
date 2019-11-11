package processing.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import processing.core.PApplet;

public class XML implements Serializable {
    protected XML[] children;
    protected Node node;
    protected XML parent;

    protected XML() {
    }

    public XML(File file) throws IOException, ParserConfigurationException, SAXException {
        this(file, (String) null);
    }

    public XML(File file, String str) throws IOException, ParserConfigurationException, SAXException {
        this((Reader) PApplet.createReader(file), str);
    }

    public XML(InputStream inputStream) throws IOException, ParserConfigurationException, SAXException {
        this(inputStream, (String) null);
    }

    public XML(InputStream inputStream, String str) throws IOException, ParserConfigurationException, SAXException {
        this((Reader) PApplet.createReader(inputStream), str);
    }

    public XML(Reader reader) throws IOException, ParserConfigurationException, SAXException {
        this(reader, (String) null);
    }

    public XML(Reader reader, String str) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        try {
            newInstance.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.valueOf(false));
        } catch (IllegalArgumentException e) {
        }
        newInstance.setExpandEntityReferences(false);
        this.node = newInstance.newDocumentBuilder().parse(new InputSource(reader)).getDocumentElement();
    }

    public XML(String str) {
        try {
            this.node = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement(str);
            this.parent = null;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    protected XML(XML xml, Node node2) {
        this.node = node2;
        this.parent = xml;
    }

    public static XML parse(String str) throws IOException, ParserConfigurationException, SAXException {
        return parse(str, null);
    }

    public static XML parse(String str, String str2) throws IOException, ParserConfigurationException, SAXException {
        return new XML((Reader) new StringReader(str), (String) null);
    }

    public XML addChild(String str) {
        return appendChild(this.node.getOwnerDocument().createElement(str));
    }

    public XML addChild(XML xml) {
        return appendChild(this.node.getOwnerDocument().importNode((Node) xml.getNative(), true));
    }

    /* access modifiers changed from: protected */
    public XML appendChild(Node node2) {
        this.node.appendChild(node2);
        XML xml = new XML(this, node2);
        if (this.children != null) {
            this.children = (XML[]) PApplet.concat((Object) this.children, (Object) new XML[]{xml});
        }
        return xml;
    }

    /* access modifiers changed from: protected */
    public void checkChildren() {
        if (this.children == null) {
            NodeList childNodes = this.node.getChildNodes();
            int length = childNodes.getLength();
            this.children = new XML[length];
            for (int i = 0; i < length; i++) {
                this.children[i] = new XML(this, childNodes.item(i));
            }
        }
    }

    public String format(int i) {
        boolean z = false;
        try {
            TransformerFactory newInstance = TransformerFactory.newInstance();
            if (i != -1) {
                try {
                    newInstance.setAttribute("indent-number", Integer.valueOf(i));
                } catch (IllegalArgumentException e) {
                    z = true;
                }
            }
            Transformer newTransformer = newInstance.newTransformer();
            if (i == -1 || this.parent == null) {
                newTransformer.setOutputProperty("omit-xml-declaration", "yes");
            } else {
                newTransformer.setOutputProperty("omit-xml-declaration", "no");
            }
            newTransformer.setOutputProperty("method", "xml");
            if (z) {
                newTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(i));
            }
            newTransformer.setOutputProperty("encoding", "UTF-8");
            newTransformer.setOutputProperty("indent", "yes");
            String property = System.getProperty("line.separator");
            StringWriter stringWriter = new StringWriter();
            newTransformer.transform(new DOMSource(this.node), new StreamResult(stringWriter));
            String[] split = PApplet.split(stringWriter.toString(), property);
            if (split[0].startsWith("<?xml")) {
                int indexOf = split[0].indexOf("?>") + 2;
                if (split[0].length() == indexOf) {
                    split = PApplet.subset(split, 1);
                } else {
                    split[0] = split[0].substring(indexOf);
                }
            }
            String join = PApplet.join(PApplet.trim(split), "");
            if (i == -1) {
                return join;
            }
            StringWriter stringWriter2 = new StringWriter();
            newTransformer.transform(new StreamSource(new StringReader(join)), new StreamResult(stringWriter2));
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + property + stringWriter2.toString();
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public int getAttributeCount() {
        return this.node.getAttributes().getLength();
    }

    public XML getChild(int i) {
        checkChildren();
        return this.children[i];
    }

    public XML getChild(String str) {
        if (str.length() > 0 && str.charAt(0) == '/') {
            throw new IllegalArgumentException("getChild() should not begin with a slash");
        } else if (str.indexOf(47) != -1) {
            return getChildRecursive(PApplet.split(str, '/'), 0);
        } else {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                XML child = getChild(i);
                String name = child.getName();
                if (name != null && name.equals(str)) {
                    return child;
                }
            }
            return null;
        }
    }

    public int getChildCount() {
        checkChildren();
        return this.children.length;
    }

    /* access modifiers changed from: protected */
    public XML getChildRecursive(String[] strArr, int i) {
        if (Character.isDigit(strArr[i].charAt(0))) {
            XML child = getChild(Integer.parseInt(strArr[i]));
            return i == strArr.length + -1 ? child : child.getChildRecursive(strArr, i + 1);
        }
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            XML child2 = getChild(i2);
            String name = child2.getName();
            if (name != null && name.equals(strArr[i])) {
                return i != strArr.length + -1 ? child2.getChildRecursive(strArr, i + 1) : child2;
            }
        }
        return null;
    }

    public XML[] getChildren() {
        checkChildren();
        return this.children;
    }

    public XML[] getChildren(String str) {
        int i;
        if (str.length() > 0 && str.charAt(0) == '/') {
            throw new IllegalArgumentException("getChildren() should not begin with a slash");
        } else if (str.indexOf(47) != -1) {
            return getChildrenRecursive(PApplet.split(str, '/'), 0);
        } else {
            if (Character.isDigit(str.charAt(0))) {
                return new XML[]{getChild(Integer.parseInt(str))};
            }
            int childCount = getChildCount();
            XML[] xmlArr = new XML[childCount];
            int i2 = 0;
            int i3 = 0;
            while (i2 < childCount) {
                XML child = getChild(i2);
                String name = child.getName();
                if (name == null || !name.equals(str)) {
                    i = i3;
                } else {
                    i = i3 + 1;
                    xmlArr[i3] = child;
                }
                i2++;
                i3 = i;
            }
            return (XML[]) PApplet.subset((Object) xmlArr, 0, i3);
        }
    }

    /* access modifiers changed from: protected */
    public XML[] getChildrenRecursive(String[] strArr, int i) {
        if (i == strArr.length - 1) {
            return getChildren(strArr[i]);
        }
        XML[] xmlArr = new XML[0];
        for (XML childrenRecursive : getChildren(strArr[i])) {
            xmlArr = (XML[]) PApplet.concat((Object) xmlArr, (Object) childrenRecursive.getChildrenRecursive(strArr, i + 1));
        }
        return xmlArr;
    }

    public String getContent() {
        return this.node.getTextContent();
    }

    public double getDouble(String str) {
        return getDouble(str, 0.0d);
    }

    public double getDouble(String str, double d) {
        String string = getString(str);
        return string == null ? d : Double.parseDouble(string);
    }

    public double getDoubleContent() {
        return getDoubleContent(0.0d);
    }

    public double getDoubleContent(double d) {
        String textContent = this.node.getTextContent();
        if (textContent == null) {
            return d;
        }
        try {
            return Double.parseDouble(textContent);
        } catch (NumberFormatException e) {
            return d;
        }
    }

    public float getFloat(String str) {
        return getFloat(str, 0.0f);
    }

    public float getFloat(String str, float f) {
        String string = getString(str);
        return string == null ? f : Float.parseFloat(string);
    }

    public float getFloatContent() {
        return getFloatContent(0.0f);
    }

    public float getFloatContent(float f) {
        return PApplet.parseFloat(this.node.getTextContent(), f);
    }

    public int getInt(String str) {
        return getInt(str, 0);
    }

    public int getInt(String str, int i) {
        String string = getString(str);
        return string == null ? i : Integer.parseInt(string);
    }

    public int getIntContent() {
        return getIntContent(0);
    }

    public int getIntContent(int i) {
        return PApplet.parseInt(this.node.getTextContent(), i);
    }

    public String getLocalName() {
        return this.node.getLocalName();
    }

    public long getLong(String str, long j) {
        String string = getString(str);
        return string == null ? j : Long.parseLong(string);
    }

    public long getLongContent() {
        return getLongContent(0);
    }

    public long getLongContent(long j) {
        String textContent = this.node.getTextContent();
        if (textContent == null) {
            return j;
        }
        try {
            return Long.parseLong(textContent);
        } catch (NumberFormatException e) {
            return j;
        }
    }

    public String getName() {
        return this.node.getNodeName();
    }

    /* access modifiers changed from: protected */
    public Object getNative() {
        return this.node;
    }

    public XML getParent() {
        return this.parent;
    }

    public String getString(String str) {
        return getString(str, null);
    }

    public String getString(String str, String str2) {
        Node namedItem = this.node.getAttributes().getNamedItem(str);
        return namedItem == null ? str2 : namedItem.getNodeValue();
    }

    public boolean hasAttribute(String str) {
        return this.node.getAttributes().getNamedItem(str) != null;
    }

    public boolean hasChildren() {
        checkChildren();
        return this.children.length > 0;
    }

    public String[] listAttributes() {
        NamedNodeMap attributes = this.node.getAttributes();
        String[] strArr = new String[attributes.getLength()];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = attributes.item(i).getNodeName();
        }
        return strArr;
    }

    public String[] listChildren() {
        checkChildren();
        String[] strArr = new String[this.children.length];
        for (int i = 0; i < this.children.length; i++) {
            strArr[i] = this.children[i].getName();
        }
        return strArr;
    }

    public void removeChild(XML xml) {
        this.node.removeChild(xml.node);
        this.children = null;
    }

    public boolean save(File file, String str) {
        PrintWriter createWriter = PApplet.createWriter(file);
        boolean write = write(createWriter);
        createWriter.flush();
        createWriter.close();
        return write;
    }

    public void setContent(String str) {
        this.node.setTextContent(str);
    }

    public void setDouble(String str, double d) {
        setString(str, String.valueOf(d));
    }

    public void setDoubleContent(double d) {
        setContent(String.valueOf(d));
    }

    public void setFloat(String str, float f) {
        setString(str, String.valueOf(f));
    }

    public void setFloatContent(float f) {
        setContent(String.valueOf(f));
    }

    public void setInt(String str, int i) {
        setString(str, String.valueOf(i));
    }

    public void setIntContent(int i) {
        setContent(String.valueOf(i));
    }

    public void setLong(String str, long j) {
        setString(str, String.valueOf(j));
    }

    public void setLongContent(long j) {
        setContent(String.valueOf(j));
    }

    public void setName(String str) {
        this.node = this.node.getOwnerDocument().renameNode(this.node, null, str);
    }

    public void setString(String str, String str2) {
        ((Element) this.node).setAttribute(str, str2);
    }

    public String toString() {
        return format(2);
    }

    public boolean write(PrintWriter printWriter) {
        printWriter.print(format(2));
        printWriter.flush();
        return true;
    }
}
