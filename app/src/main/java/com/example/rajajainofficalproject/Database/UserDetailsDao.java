package com.example.rajajainofficalproject.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDetailsDao {

    @Insert
    void insertDetails(UserDetails userDetailsModelClass);

    @Query("SELECT * FROM User_Details WHERE email = :demoEmail")
    List<UserDetails> findDetails(String demoEmail);

    @Query("DELETE FROM User_Details WHERE unique_ID = :user_unique_id")
    void deleteDetails(String user_unique_id);

    @Query("SELECT unique_ID,userID,name,image,email,number,password FROM User_Details WHERE unique_ID =:user_unique_id")
    List<UserDetails> getAllDetails(String user_unique_id);

//    @Update
//    void  updateDetails(UserDetails userDetails);

    //Below code for update details for table created;
    @Query("UPDATE User_Details SET unique_ID=:user_unique_id, email=:demoEmail,  name=:name, number=:number, password=:password, image =:image  WHERE unique_ID = :user_unique_id")
    void  updateDetails(String user_unique_id, String name,String image, String demoEmail,  String number, String password );

}
