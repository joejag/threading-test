package suncertify.db.file.reader;

import suncertify.db.DatabaseException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.file.meta.DatabaseRow;
import suncertify.db.file.meta.LogicalDelete;
import suncertify.db.file.meta.MetaData;
import suncertify.db.file.meta.MetaDataReader;
import suncertify.lang.ByteStreamUtils;
import suncertify.lang.CollectionToArray;
import suncertify.lang.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SimpleFileDatabaseReader implements FileDatabaseReader
{
   private static final Logger log = Logger.getLogger(SimpleFileDatabaseReader.class.getName());

   private MetaData metaData;
   private RandomAccessFile database;

   public SimpleFileDatabaseReader(File file) throws FileNotFoundException, DatabaseException
   {
      log.info("FileDatabaseReader created using : " + file.getAbsolutePath());

      database = new RandomAccessFile(file, "rw");
      metaData = new MetaDataReader().setup(database);
   }

   public synchronized List<DatabaseRow> readAll()
   {
      List<DatabaseRow> result = new ArrayList<DatabaseRow>();

      for (int recordNumber = 0; recordNumber < getInternalRowCount(); recordNumber++)
      {
         try
         {
            result.add(new DatabaseRow(recordNumber, isDeleted(recordNumber), read(recordNumber)));
         } catch (RecordNotFoundException e)
         {
            throw new DatabaseException("Cannot read expected row: " + e.getMessage(), e);
         }
      }

      return result;
   }

   synchronized String[] read(int recordNumber) throws RecordNotFoundException
   {
      List<String> result = new ArrayList<String>();

      moveToRow(recordNumber);

      currentRowIsDeleted();
//    if (currentRowIsDeleted())
//      throw new RecordNotFoundException("Record has been deleted: " + recordNumber);

      Map<String, Short> columns = metaData.getColumns();
      for (String column : columns.keySet())
      {
         try
         {
            result.add(ByteStreamUtils.readStringOfSize(database, columns.get(column)).trim());
         } catch (IOException e)
         {
            throw new DatabaseException("Cannot read column: " + column + " because: " + e.getMessage(), e);
         }
      }

      return CollectionToArray.convert(result);
   }

   public synchronized int create(String[] content)
   {
      try
      {
         database.seek(database.length());
         persist(content);

         return getInternalRowCount();
      } catch (IOException e)
      {
         throw new DatabaseException("Cannot move to end of file: " + e.getMessage(), e);
      }
   }

   public synchronized void update(int recordNumber, String[] content) throws RecordNotFoundException
   {
      if (isDeleted(recordNumber))
         throw new RecordNotFoundException("Record has been deleted: " + recordNumber);

      moveToRow(recordNumber);
      persist(content);
   }

   private void persist(String[] content)
   {
      try
      {
         database.write(LogicalDelete.VALID.getValue());

         List<Short> fieldSizes = metaData.getFieldSizes();

         for (int i = 0; i < content.length; i++)
            database.write(StringUtils.padRight(content[i], fieldSizes.get(i)).getBytes());

      } catch (IOException e)
      {
         throw new DatabaseException("Cannot write to disk: " + e.getMessage(), e);
      }
   }

   public synchronized void delete(int recordNumber) throws RecordNotFoundException
   {
      moveToRow(recordNumber);
      try
      {
         database.write(LogicalDelete.DELETED.getValue());
      } catch (IOException e)
      {
         throw new DatabaseException("Cannot delete row: " + e.getMessage(), e);
      }
   }

   private int getInternalRowCount()
   {
      try
      {
         long sizeWithoutHeading = database.length() - metaData.getDataStartPointer();
         return (int) (sizeWithoutHeading / metaData.getSizeOfRecord());
      } catch (IOException e)
      {
         throw new DatabaseException("Cannot get file size: " + e.getMessage(), e);
      }
   }

   private boolean currentRowIsDeleted()
   {
      try
      {
         return LogicalDelete.isDeleted(database.readByte());
      } catch (IOException e)
      {
         throw new DatabaseException("Cannot get deleted flag for row: " + e.getMessage(), e);
      }
   }

   private boolean isDeleted(int recordNumber)
   {
      try
      {
         try
         {
            moveToRow(recordNumber);
         } catch (RecordNotFoundException e)
         {
            return true;
         }

         return LogicalDelete.isDeleted(database.readByte());
      } catch (IOException e)
      {
         throw new DatabaseException("Cannot get deleted flag for row: " + e.getMessage(), e);
      }
   }

   private void moveToRow(int recordNumber) throws RecordNotFoundException
   {
      try
      {
         long pointInFileForRow = (recordNumber * metaData.getSizeOfRecord()) + metaData.getDataStartPointer();

         if (pointInFileForRow > database.length())
            throw new RecordNotFoundException("RecordNumber is too large: " + recordNumber);

         database.seek(pointInFileForRow);
      } catch (IOException e)
      {
         throw new DatabaseException("Cannot seek in file: " + e.getMessage(), e);
      }
   }
}
