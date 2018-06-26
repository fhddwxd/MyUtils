package com.wxd.myutils.SoapService;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public interface ISoapService {
	Object LoadResult() throws IOException, XmlPullParserException;
}
