package com.haseman.serviceExample;

interface IMusicService
{
	void pause();
    void play();
	void setDataSource(in long id);
	String getSongTitle();
}