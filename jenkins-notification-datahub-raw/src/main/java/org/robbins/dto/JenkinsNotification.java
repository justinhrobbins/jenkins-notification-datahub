package org.robbins.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonIgnoreProperties(ignoreUnknown=true)
public class JenkinsNotification
{
    @XmlElement
    private String name;

    @XmlElement
    private Build build;

    @XmlElement
    private String url;

	public String getName ()
	{
		return name;
	}

	public void setName (String name)
	{
		this.name = name;
	}

	public Build getBuild ()
	{
		return build;
	}

	public void setBuild (Build build)
	{
		this.build = build;
	}

	public String getUrl ()
	{
		return url;
	}

	public void setUrl (String url)
	{
		this.url = url;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [name = "+name+", build = "+build+", url = "+url+"]";
	}
}
