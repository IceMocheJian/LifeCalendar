package com.example.lifecalendar

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns

class LifeCalendarProvider : ContentProvider() {

    private lateinit var dbHelper: LifespanDbHelper
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    companion object {
        const val AUTHORITY = "com.example.lifecalendar.provider" // 替换为你的应用包名
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/lifespan")

        const val LIFESPAN_TABLE = "lifespan"
        const val LIFESPAN_COLUMN_WEEKS = "weeks"

        const val LIFESPAN_ID = 1
    }

    init {
        uriMatcher.addURI(AUTHORITY, "lifespan", LIFESPAN_ID)
    }

    override fun onCreate(): Boolean {
        dbHelper = LifespanDbHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        val cursor = when (uriMatcher.match(uri)) {
            LIFESPAN_ID -> db.query(
                LIFESPAN_TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            LIFESPAN_ID -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$LIFESPAN_TABLE"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val id = when (uriMatcher.match(uri)) {
            LIFESPAN_ID -> db.insert(LIFESPAN_TABLE, null, values)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        if (id > 0) {
            context?.contentResolver?.notifyChange(uri, null)
            return Uri.withAppendedPath(CONTENT_URI, id.toString())
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        val rowsDeleted = when (uriMatcher.match(uri)) {
            LIFESPAN_ID -> db.delete(LIFESPAN_TABLE, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        if (rowsDeleted > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        return rowsDeleted
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val db = dbHelper.writableDatabase
        val rowsUpdated = when (uriMatcher.match(uri)) {
            LIFESPAN_ID -> db.update(LIFESPAN_TABLE, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        if (rowsUpdated > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        return rowsUpdated
    }

    private class LifespanDbHelper(context: android.content.Context) :
        SQLiteOpenHelper(context, "lifespan.db", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE $LIFESPAN_TABLE (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$LIFESPAN_COLUMN_WEEKS INTEGER)"
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $LIFESPAN_TABLE")
            onCreate(db)
        }
    }
}