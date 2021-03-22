package com.example.rajajainofficalproject.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserDetails.class}, version = 6)
public abstract  class UserDetailsRoomDatabase extends RoomDatabase {
    public abstract UserDetailsDao productDao();
    private static UserDetailsRoomDatabase INSTANCE;

    public static UserDetailsRoomDatabase getDatabase( Context context) {
        if (INSTANCE == null) {
            synchronized (UserDetailsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserDetailsRoomDatabase.class,
                            "user_details_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
