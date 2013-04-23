package com.jspsmart.upload;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;

public class File
{
  private SmartUpload m_parent;
  private int m_startData = 0;
  private int m_endData = 0;
  private int m_size = 0;
  private String m_fieldname = new String();
  private String m_filename = new String();
  private String m_fileExt = new String();
  private String m_filePathName = new String();
  private String m_contentType = new String();
  private String m_contentDisp = new String();
  private String m_typeMime = new String();
  private String m_subTypeMime = new String();
  private String m_contentString = new String();
  private boolean m_isMissing = true;
  public static final int SAVEAS_AUTO = 0;
  public static final int SAVEAS_VIRTUAL = 1;
  public static final int SAVEAS_PHYSICAL = 2;

  public void saveAs(String paramString)
    throws IOException, SmartUploadException
  {
    saveAs(paramString, 0);
  }

  public void saveAs(String paramString, int paramInt)
    throws IOException, SmartUploadException
  {
    String str = new String();
    str = this.m_parent.getPhysicalPath(paramString, paramInt);

    if (str == null) {
      throw new IllegalArgumentException("There is no specified destination file (1140).");
    }

    try
    {
      java.io.File localFile = new java.io.File(str);

      FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
      localFileOutputStream.write(this.m_parent.m_binArray, this.m_startData, this.m_size);
      localFileOutputStream.close();
    }
    catch (IOException localIOException) {
      throw new SmartUploadException("File can't be saved (1120).");
    }
  }

  public void fileToField(ResultSet paramResultSet, String paramString)
    throws ServletException, IOException, SmartUploadException, SQLException
  {
    long l = 0L;
    int i = 65536;
    int j = 0;

    int k = this.m_startData;

    if (paramResultSet == null) throw new IllegalArgumentException("The RecordSet cannot be null (1145).");

    if (paramString == null) throw new IllegalArgumentException("The columnName cannot be null (1150).");

    if (paramString.length() == 0) throw new IllegalArgumentException("The columnName cannot be empty (1155).");

    l = BigInteger.valueOf(this.m_size).divide(BigInteger.valueOf(i)).longValue();

    j = BigInteger.valueOf(this.m_size).mod(BigInteger.valueOf(i)).intValue();
    try
    {
      for (int i1 = 1; i1 < l; ++i1) {
        paramResultSet.updateBinaryStream(paramString, new ByteArrayInputStream(this.m_parent.m_binArray, k, i), i);

        k = (k == 0) ? 1 : k;

        k = i1 * i + this.m_startData;
      }

      if (j > 0) {
        paramResultSet.updateBinaryStream(paramString, new ByteArrayInputStream(this.m_parent.m_binArray, k, j), j);
      }

    }
    catch (SQLException localSQLException)
    {
      byte[] arrayOfByte = new byte[this.m_size];
      System.arraycopy(this.m_parent.m_binArray, this.m_startData, arrayOfByte, 0, this.m_size);

      paramResultSet.updateBytes(paramString, arrayOfByte);
    }
    catch (Exception localException) {
      throw new SmartUploadException("Unable to save file in the DataBase (1130).");
    }
  }

  public boolean isMissing()
  {
    return this.m_isMissing;
  }

  public String getFieldName()
  {
    return this.m_fieldname;
  }

  public String getFileName()
  {
    return this.m_filename;
  }

  public String getFilePathName()
  {
    return this.m_filePathName;
  }

  public String getFileExt()
  {
    return this.m_fileExt;
  }

  public String getContentType()
  {
    return this.m_contentType;
  }

  public String getContentDisp()
  {
    return this.m_contentDisp;
  }

  public String getContentString()
  {
    String str = new String(this.m_parent.m_binArray, this.m_startData, this.m_size);
    return str;
  }

  public String getTypeMIME()
    throws IOException
  {
    return this.m_typeMime;
  }

  public String getSubTypeMIME()
  {
    return this.m_subTypeMime;
  }

  public int getSize()
  {
    return this.m_size;
  }

  protected int getStartData()
  {
    return this.m_startData;
  }

  protected int getEndData()
  {
    return this.m_endData;
  }

  protected void setParent(SmartUpload paramSmartUpload)
  {
    this.m_parent = paramSmartUpload;
  }

  protected void setStartData(int paramInt)
  {
    this.m_startData = paramInt;
  }

  protected void setEndData(int paramInt)
  {
    this.m_endData = paramInt;
  }

  protected void setSize(int paramInt)
  {
    this.m_size = paramInt;
  }

  protected void setIsMissing(boolean paramBoolean)
  {
    this.m_isMissing = paramBoolean;
  }

  protected void setFieldName(String paramString)
  {
    this.m_fieldname = paramString;
  }

  protected void setFileName(String paramString)
  {
    this.m_filename = paramString;
  }

  protected void setFilePathName(String paramString)
  {
    this.m_filePathName = paramString;
  }

  protected void setFileExt(String paramString)
  {
    this.m_fileExt = paramString;
  }

  protected void setContentType(String paramString)
  {
    this.m_contentType = paramString;
  }

  protected void setContentDisp(String paramString)
  {
    this.m_contentDisp = paramString;
  }

  protected void setTypeMIME(String paramString)
  {
    this.m_typeMime = paramString;
  }

  protected void setSubTypeMIME(String paramString)
  {
    this.m_subTypeMime = paramString;
  }

  public byte getBinaryData(int paramInt)
  {
    if (this.m_startData + paramInt > this.m_endData) {
      throw new ArrayIndexOutOfBoundsException("Index Out of range (1115).");
    }

    if (this.m_startData + paramInt <= this.m_endData)
      return this.m_parent.m_binArray[(this.m_startData + paramInt)];
    return 0;
  }
}