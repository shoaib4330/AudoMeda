package com.emo.lkplayer.innerlayer.model.entities.retroModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class retro_TrackSearchResponse {

    public Message message;

    public class Message {

        @SerializedName("header")
        @Expose
        public Header header;
        @SerializedName("body")
        @Expose
        public Body body;

        public class Header {

            @SerializedName("status_code")
            @Expose
            public Integer statusCode;
            @SerializedName("execute_time")
            @Expose
            public Double executeTime;
            @SerializedName("available")
            @Expose
            public Integer available;

        }

        public class Body {

            @SerializedName("track_list")
            @Expose
            public List trackList = new ArrayList();
        }
    }
}
