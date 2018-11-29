package org.izv.aad.chatterbot_database.DBConnection;

import org.izv.aad.proyectotrimestre.POJO.Author;
import org.izv.aad.proyectotrimestre.POJO.Readings;

public class DBManager {

    public static void insert(ReadingsManager readingsManager, FireBaseConnection fbc, Readings reading){
        readingsManager.insert(reading);
        fbc.saveReading(reading);
    }

    public static void insert(ChatManager chatManager, FireBaseConnection fbc, Author author){
        chatManager.insert(author);
        fbc.saveAuthor(author);
    }

    public static void delete(ReadingsManager readingsManager, FireBaseConnection fbc, Readings reading){
        readingsManager.delete(reading.getId());
        fbc.deleteReading(reading);
    }

    public static void delete(ChatManager chatManager, FireBaseConnection fbc, Author author){
        chatManager.delete(author.getId());
        fbc.deleteAuthor(author);
    }

    public static void syncronize(ReadingsManager readingsManager, ChatManager chatManager, FireBaseConnection fbc){

    }
}
