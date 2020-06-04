package com.wxd.myutils.soapService;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public interface ISoapService {
	Object LoadResult() throws IOException, XmlPullParserException;
}
