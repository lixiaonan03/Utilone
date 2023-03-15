package com.lxn.resourceplugin;

/**
  *  @author lixiaonan
  *  功能描述: 学习比人的处理
  *  时 间： 2023/3/8 15:15
  */
public abstract class Resource {
    protected boolean isValueType;
    protected String lastDirectory;
    
    public String getLastDirectory() {
        return lastDirectory;
    }
    
    public void setLastDirectory(String lastDirectory) {
        this.lastDirectory = lastDirectory;
    }
    
    public boolean isValueType() {
        return isValueType;
    }
    
    public abstract String getUniqueId();
    
    public abstract String belongFilePath();
    
    public abstract boolean compare(Resource obj);
}
