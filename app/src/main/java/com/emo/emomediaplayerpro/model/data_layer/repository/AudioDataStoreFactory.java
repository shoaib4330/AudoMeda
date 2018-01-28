package com.emo.emomediaplayerpro.model.data_layer.repository;

import android.content.Context;

import com.emo.emomediaplayerpro.model.data_layer.daos.LocalAudioTracksDao;

/**
 * Created by shoaibanwar on 11/22/17.
 */

public class AudioDataStoreFactory {

    /* ToDo: Yahan, ek logic honi chahiye, jo decide kre k konsa datastore return krna he,
    localDB ka ya Cloud ka ya Api ka. Sb datastores ko same interface implement krhe hona chahiye.
    Aur wahi interface is factory class k methods ki return type honi chahiye. Filhal men ye kam nahin
    krha... Jb kbhi multiple datasources dale jayenge, to ye b dekhlenge.

    Plus, Factory ko knowledge hoga k konse datastore ko kese initialize krna he. Aur yehi bna kr
    return kia kregi as interface typecasted datasource. Kisi aur class ko ye knowledge hona nahi
    chahiye...
     */

    public LocalAudioTracksDao getLocalDatastore(Context context)
    {
        return new LocalAudioTracksDao(context);
    }
}
