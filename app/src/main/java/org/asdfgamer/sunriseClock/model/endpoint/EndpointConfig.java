package org.asdfgamer.sunriseClock.model.endpoint;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EndpointConfig {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String connectionString;

    public String addedAt;
}
