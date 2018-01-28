package com.emo.emomediaplayerpro.model.domain.retro_entities;

/**
 * Created by shoaibanwar on 11/28/17.
 */

public class retro_CloudTrack {

    private long id;
    private String title;
    private String link;
    private String image_link;
    private String created;
    private String updated;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getImage_link()
    {
        return image_link;
    }

    public void setImage_link(String image_link)
    {
        this.image_link = image_link;
    }

    public String getCreated()
    {
        return created;
    }

    public void setCreated(String created)
    {
        this.created = created;
    }

    public String getUpdated()
    {
        return updated;
    }

    public void setUpdated(String updated)
    {
        this.updated = updated;
    }
}
