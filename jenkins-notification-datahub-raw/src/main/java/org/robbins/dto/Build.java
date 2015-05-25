package org.robbins.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Build
{
    @XmlElement
    private String status;

    @XmlElement
    private String number;

    @XmlElement
    private String full_url;

    @XmlElement
    private String phase;

    @XmlElement
    private String url;

	public String getStatus ()
	{
		return status;
	}

	public void setStatus (String status)
	{
		this.status = status;
	}

	public String getNumber ()
	{
		return number;
	}

	public void setNumber (String number)
	{
		this.number = number;
	}

	public String getFull_url ()
	{
		return full_url;
	}

	public void setFull_url (String full_url)
	{
		this.full_url = full_url;
	}

	public String getPhase ()
	{
		return phase;
	}

	public void setPhase (String phase)
	{
		this.phase = phase;
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
		return "Build{" +
				"status='" + status + '\'' +
				", number='" + number + '\'' +
				", full_url='" + full_url + '\'' +
				", phase='" + phase + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
