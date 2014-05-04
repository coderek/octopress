public class DataBaseHelper extends SQLiteOpenHelper{

    //The Android’s default system path of your application database.
  private static String DB_PATH = “/data/data/YOUR_PACKAGE/databases/”;
  private static String DB_NAME = “myDBName”;
  private SQLiteDatabase myDataBase; 
  private final Context myContext;

  public DataBaseHelper(Context context) {
    super(context, DB_NAME, null, 1);
    this.myContext = context;
  }

  public void createDataBase() throws IOException {
    boolean dbExist = checkDataBase();
    if (dbExist) {
      //do nothing – database already exist
    } else {
      this.getReadableDatabase();
      try {
        copyDataBase(); 
      } catch (IOException e) {
        throw new Error(“Error copying database”);
      }
    }
  }

  private boolean checkDataBase(){
    SQLiteDatabase checkDB = null;
    try{
      String myPath = DB_PATH + DB_NAME;
      checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }catch(SQLiteException e){
    //database does’t exist yet.
    }

    if(checkDB != null){
      checkDB.close();
    }
    return checkDB != null ? true : false;
  }

  private void copyDataBase() throws IOException{

    //Open your local db as the input stream
    InputStream myInput = myContext.getAssets().open(DB_NAME);
    // Path to the just created empty db
    String outFileName = DB_PATH + DB_NAME;
    //Open the empty db as the output stream
    OutputStream myOutput = new FileOutputStream(outFileName);
    //transfer bytes from the inputfile to the outputfile
    byte[] buffer = new byte[1024];
    int length;
    while ((length = myInput.read(buffer))>0){
      myOutput.write(buffer, 0, length);
    }
    //Close the streams
    myOutput.flush();
    myOutput.close();
    myInput.close();
  }

  public void openDataBase() throws SQLException{
    //Open the database
    String myPath = DB_PATH + DB_NAME;
    myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
  }

  @Override
  public synchronized void close() {
    if(myDataBase != null)
      myDataBase.close();
    super.close();
  }

  @Override
  public void onCreate(SQLiteDatabase db) {

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
