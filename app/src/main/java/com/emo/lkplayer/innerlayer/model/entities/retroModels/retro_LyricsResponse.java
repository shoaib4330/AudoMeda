package com.emo.lkplayer.innerlayer.model.entities.retroModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shoaibanwar on 8/5/17.
 */

public class retro_LyricsResponse {

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

        }

        public class Body {

            @SerializedName("lyrics")
            @Expose
            public Lyrics lyrics;

            public class Lyrics {

                @SerializedName("lyrics_id")
                @Expose
                public Integer lyricsId;
                @SerializedName("restricted")
                @Expose
                public Integer restricted;
                @SerializedName("instrumental")
                @Expose
                public Integer instrumental;
                @SerializedName("lyrics_body")
                @Expose
                public String lyricsBody;
                @SerializedName("lyrics_language")
                @Expose
                public String lyricsLanguage;
                @SerializedName("script_tracking_url")
                @Expose
                public String scriptTrackingUrl;
                @SerializedName("pixel_tracking_url")
                @Expose
                public String pixelTrackingUrl;
                @SerializedName("html_tracking_url")
                @Expose
                public String htmlTrackingUrl;
                @SerializedName("lyrics_copyright")
                @Expose
                public String lyricsCopyright;
                @SerializedName("updated_time")
                @Expose
                public String updatedTime;

            }
        }
    }
}
