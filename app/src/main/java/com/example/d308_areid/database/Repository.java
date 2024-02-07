package com.example.d308_areid.database;

import android.app.Application;
import android.util.Log;

import com.example.d308_areid.dao.ExcursionDAO;
import com.example.d308_areid.dao.VacationDAO;
import com.example.d308_areid.entities.Excursions;
import com.example.d308_areid.entities.Vacations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private final ExcursionDAO mExcursionDAO;
    private final VacationDAO mVacationDAO;

    private List<Vacations> mAllVacations;
    private List<Excursions> mAllExcursions;

    private static final int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public Repository(Application application){
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO=db.excursionDAO();
        mVacationDAO=db.vacationDAO();
    }


    public List<Vacations> getmAllVacations(){
        databaseExecutor.execute(()->{
            mAllVacations=mVacationDAO.getAllVacations();

        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllVacations;
    }



    public void insert(Vacations vacations){
        Log.d("Repository", "Inserting vacation: " + vacations.getVacationName());
        databaseExecutor.execute(()->{
            mVacationDAO.insert(vacations);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Vacations vacations){
        Log.d("Repository", "Updating vacation: " + vacations.getVacationName());
        databaseExecutor.execute(()->{
            mVacationDAO.update(vacations);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Vacations vacations){
        databaseExecutor.execute(()->{
            mVacationDAO.delete(vacations);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Excursions> getmAllExcursions(){
        databaseExecutor.execute(()->{
            mAllExcursions=mExcursionDAO.getAllExcursions();

        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllExcursions;
    }

    public List<Excursions> getAssociatedExcursions(int vacationID){
        databaseExecutor.execute(()->{
            mAllExcursions=mExcursionDAO.getAssociatedExcursions(vacationID);

        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllExcursions;
    }



    public void insert(Excursions excursions){
        databaseExecutor.execute(()->{
            mExcursionDAO.insert(excursions);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Excursions excursions){
        databaseExecutor.execute(()->{
            mExcursionDAO.update(excursions);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Excursions excursions){
        databaseExecutor.execute(()->{
            mExcursionDAO.delete(excursions);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
