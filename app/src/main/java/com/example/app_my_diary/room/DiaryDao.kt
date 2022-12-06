package com.example.app_my_diary.room

import androidx.room.*
import com.example.app_my_diary.model.DiaryModel

@Dao
interface DiaryDao {
    @Insert
    suspend fun addDiary(diaryModel: DiaryModel)

    @Update
    suspend fun updateDiary(diaryModel: DiaryModel)

    @Delete
    suspend fun deleteDiary(diaryModel: DiaryModel)

    @Query("Select * from tblDiary order by time DESC")
    suspend fun getAllDiary(): List<DiaryModel>

    @Query("Select * from tblDiary  where diary_id < (:idOld) order by diary_id DESC limit 1")
    suspend fun getDiaryPrevious(idOld :Int): DiaryModel

    @Query("Select * from tblDiary  where diary_id < (:idOld) order by diary_id DESC limit 1")
    suspend fun checkPrevious(idOld :Int): DiaryModel

    @Query("Select * from tblDiary  where diary_id > (:idOld) order by diary_id ASC limit 1")
    suspend fun checkNext(idOld :Int): DiaryModel

    @Query("Select * from tblDiary  where diary_id > (:idOld) order by diary_id ASC limit 1")
    suspend fun getDiaryNext(idOld :Int): DiaryModel

    @Query("Select * from tblDiary  where diary_id = (:id)")
    suspend fun getDiaryCurrent(id :Int): DiaryModel

    @Query("select count(*) from tblDiary where time >= (:dateTime) and time <= (:dateTime + 86400000)")
    suspend fun checkDateHasDiary(dateTime: Long): Int

    @Query("select * from tblDiary where time >= (:dateTime) and time <= (:dateTime + 86400000)")
    suspend fun getDiaryByDate(dateTime: Long): List<DiaryModel>

    @Query("select * from tblDiary where (title like '%' || :keyword || '%') or (description like '%' || :keyword || '%') ")
    suspend fun getDiaryByKeyword(keyword: String): List<DiaryModel>
}