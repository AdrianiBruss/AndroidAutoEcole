package com.mpdam.ronald.autoecole.models;

import com.strongloop.android.loopback.ModelRepository;

/**
 * Created by Ronald on 26/07/2016.
 */

public class VoitureRepository extends ModelRepository<Voiture> {
    public VoitureRepository() {
        super("voiture", Voiture.class);
    }
}
