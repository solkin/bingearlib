package com.tomclaw.bingear;

import com.tomclaw.utils.ArrayUtil;
import com.tomclaw.utils.StringUtil;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Solkin Igor Viktorovich, TomClaw Software, 2003-2012
 * http://www.tomclaw.com/
 * @author Игорь
 */
public class BinGear {

    public Hashtable hashtable;

    public BinGear() {
        hashtable = new Hashtable();
    }

    public void addGroup(String groupName) throws IncorrectValueException {
        if (groupName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        hashtable.put(groupName, new Hashtable());
    }

    public void addItem(String groupName, String itemName, String value) throws GroupNotFoundException, IncorrectValueException {
        if (groupName == null || itemName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        try {
            ((Hashtable) hashtable.get(groupName)).put(itemName, value);
        } catch (NullPointerException ex1) {
            throw new GroupNotFoundException(groupName.concat(" is not exist"));
        }
    }

    public Hashtable getGroup(String groupName) throws IncorrectValueException, GroupNotFoundException {
        if (groupName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        try {
            return (Hashtable) hashtable.get(groupName);
        } catch (NullPointerException ex1) {
            throw new GroupNotFoundException(groupName.concat(" is not exist"));
        }
    }

    public String getValue(String groupName, String itemName) throws GroupNotFoundException, IncorrectValueException {
        if (groupName == null || itemName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        try {
            return (String) ((Hashtable) hashtable.get(groupName)).get(itemName);
        } catch (NullPointerException ex1) {
            throw new GroupNotFoundException(groupName.concat(" is not exist"));
        }
    }

    public String getValue(String groupName, String itemName, boolean isFullCompare) throws GroupNotFoundException, IncorrectValueException {
        if (groupName == null || itemName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        if (isFullCompare || hashtable.containsKey(groupName)) {
            return getValue(groupName, itemName);
        } else {
            try {
                Enumeration groupKeys = hashtable.keys();
                String tempName;
                for (; groupKeys.hasMoreElements();) {
                    tempName = (String) groupKeys.nextElement();
                    if (tempName.startsWith(groupName) || groupName.startsWith(tempName)) {
                        return (String) ((Hashtable) hashtable.get(tempName)).get(itemName);
                    }
                }
            } catch (NullPointerException ex1) {
                throw new GroupNotFoundException(groupName.concat(" is not exist"));
            }
        }
        return null;
    }

    public void renameGroup(String groupOldName, String groupNewName) throws IncorrectValueException, GroupNotFoundException {
        if (groupOldName == null || groupNewName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        try {
            hashtable.put(groupNewName, hashtable.get(groupOldName));
            hashtable.remove(groupOldName);
        } catch (NullPointerException ex1) {
            throw new GroupNotFoundException(groupOldName.concat(" is not exist"));
        }
    }

    public String[] listGroups() {
        String[] groups = new String[hashtable.size()];
        Enumeration groupKeys = hashtable.keys();
        String groupName;
        for (int c = 0; groupKeys.hasMoreElements(); c++) {
            groupName = (String) groupKeys.nextElement();
            groups[c] = groupName;
        }
        return groups;
    }

    public String[] listItems(String groupName) throws IncorrectValueException, GroupNotFoundException {
        if (groupName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        try {
            String[] items = new String[((Hashtable) hashtable.get(groupName)).size()];
            Enumeration itemKeys = ((Hashtable) hashtable.get(groupName)).keys();
            for (int c = 0; itemKeys.hasMoreElements(); c++) {
                groupName = (String) itemKeys.nextElement();
                items[c] = groupName;
            }
            return items;
        } catch (NullPointerException ex1) {
            throw new GroupNotFoundException(groupName.concat(" is not exist"));
        }
    }

    public String[] listItems(String groupName, boolean isFullCompare) throws IncorrectValueException, GroupNotFoundException {
        if (groupName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        if (isFullCompare || hashtable.containsKey(groupName)) {
            return listItems(groupName);
        } else {
            try {
                Enumeration groupKeys = hashtable.keys();
                String tempName;
                for (; groupKeys.hasMoreElements();) {
                    tempName = (String) groupKeys.nextElement();
                    if (tempName.startsWith(groupName) || groupName.startsWith(tempName)) {
                        String[] items = new String[((Hashtable) hashtable.get(tempName)).size()];
                        Enumeration itemKeys = ((Hashtable) hashtable.get(tempName)).keys();
                        for (int c = 0; itemKeys.hasMoreElements(); c++) {
                            items[c] = (String) itemKeys.nextElement();
                        }
                        return items;
                    }
                }
                return null;
            } catch (NullPointerException ex1) {
                throw new GroupNotFoundException(groupName.concat(" is not exist"));
            }
        }
    }

    public void renameItem(String groupName, String itemOldName, String itemNewName) throws IncorrectValueException, GroupNotFoundException {
        if (groupName == null || itemOldName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        try {
            ((Hashtable) hashtable.get(groupName)).put(itemNewName, ((Hashtable) hashtable.get(groupName)).get(itemOldName));
            ((Hashtable) hashtable.get(groupName)).remove(itemOldName);
        } catch (NullPointerException ex1) {
            throw new GroupNotFoundException(groupName.concat(" is not exist"));
        }
    }

    public void setValue(String groupName, String itemName, String value) throws GroupNotFoundException, IncorrectValueException {
        if (groupName == null || itemName == null) {
            throw new IncorrectValueException("nulltype is not allowed here as patameter");
        }
        try {
            ((Hashtable) hashtable.get(groupName)).put(itemName, value);
        } catch (NullPointerException ex1) {
            throw new GroupNotFoundException(groupName.concat(" is not exist"));
        }
    }

    public void removeGroup(String groupName) {
        hashtable.remove(groupName);
    }

    public void removeItem(String groupName, String itemName) {
        ((Hashtable) hashtable.get(groupName)).remove(itemName);
    }

    public void exportToIni(OutputStream outputStream) throws IOException {
        Enumeration groupKeys = hashtable.keys();
        Enumeration itemKeys;
        String itemName;
        String groupName;
        for (; groupKeys.hasMoreElements();) {
            groupName = (String) groupKeys.nextElement();
            outputStream.write(StringUtil.stringToByteArray("[".concat(groupName).concat("]\n"), true));
            itemKeys = ((Hashtable) hashtable.get(groupName)).keys();
            for (; itemKeys.hasMoreElements();) {
                itemName = (String) itemKeys.nextElement();
                outputStream.write(StringUtil.stringToByteArray(itemName.concat("=").concat((String) ((Hashtable) hashtable.get(groupName)).get(itemName)).concat("\n"), true));
            }
        }
        outputStream.flush();
    }

    public void saveToDat(DataOutputStream outputStream) throws IOException {
        /**
         *   [int] groupsCount
         * г [int] groupNameLength
         * | [String] groupName
         * | [int] itemsCount
         * | [int] itemNameLength
         * | [String] itemName
         * | [int] valueLength
         * L [String] value
         * г ----
         * | ...
         * L ----
         * ...
         */
        Enumeration groupKeys = hashtable.keys();
        Enumeration itemKeys;
        String itemName;
        String groupName;
        String value;
        /** Groups count **/
        outputStream.writeChar(hashtable.size());
        for (; groupKeys.hasMoreElements();) {
            groupName = (String) groupKeys.nextElement();
            /** Group name **/
            outputStream.writeUTF(groupName);
            itemKeys = ((Hashtable) hashtable.get(groupName)).keys();
            /** Items count **/
            outputStream.writeChar(((Hashtable) hashtable.get(groupName)).size());
            for (; itemKeys.hasMoreElements();) {
                itemName = (String) itemKeys.nextElement();
                /** Item name **/
                outputStream.writeUTF(itemName);
                value = (String) ((Hashtable) hashtable.get(groupName)).get(itemName);
                /** Value **/
                outputStream.writeUTF(value);
            }
        }
        outputStream.flush();
    }

    public void readFromDat(DataInputStream inputStream) throws IOException, IncorrectValueException, GroupNotFoundException, EOFException {
        /**
         *   [int] groupsCount
         * г [int] groupNameLength
         * | [String] groupName
         * | [int] itemsCount
         * | [int] itemNameLength
         * | [String] itemName
         * | [int] valueLength
         * L [String] value
         * г ----
         * | ...
         * L ----
         * ...
         */
        hashtable.clear();
        int groupCount = inputStream.readChar();
        String groupName;
        String itemName;
        String value;
        for (int c = 0; c < groupCount; c++) {
            groupName = inputStream.readUTF();
            addGroup(groupName);
            int itemsCount = inputStream.readChar();
            for (int i = 0; i < itemsCount; i++) {
                itemName = inputStream.readUTF();
                value = inputStream.readUTF();
                addItem(groupName, itemName, value);
            }
        }
    }

    public void importFromIni(DataInputStream inputStream) throws IOException, IncorrectValueException, GroupNotFoundException, EOFException, Throwable {
        hashtable.clear();
        byte ch;
        String prevHeader = null;
        boolean isFirstIndex = true;
        ArrayUtil buffer = new ArrayUtil();
        while (inputStream.available() > 0 && (ch = inputStream.readByte()) != -1) {
            if (ch == 13) {
                continue;
            }
            if (ch == 10) {
                if (buffer.length() <= 1) {
                    continue;
                }
                if (buffer.byteString[0] == '[' && buffer.byteString[buffer.length() - 1] == ']') {
                    prevHeader = StringUtil.byteArrayToString(buffer.subarray(1, buffer.length() - 1), true);
                    addGroup(prevHeader);
                } else {
                    int equivIndex;
                    if (isFirstIndex) {
                        equivIndex = buffer.indexOf('=');
                    } else {
                        equivIndex = buffer.lastIndexOf('=');
                    }
                    if (equivIndex > 0) {
                        addItem(prevHeader,
                                StringUtil.byteArrayToString(buffer.subarray(0, equivIndex), true),
                                StringUtil.byteArrayToString(buffer.subarray(equivIndex + 1, buffer.length()), true));
                    }
                }
                buffer.clear();
                continue;
            }
            buffer.append(ch);
        }
    }
}
