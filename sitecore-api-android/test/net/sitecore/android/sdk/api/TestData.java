package net.sitecore.android.sdk.api;

public interface TestData {

    String RSA_PUBLIC_KEY = "<RSAKeyValue><Modulus>meGMJBAnst3cKUENZo5nzxVoG8WtuNl4GmrQxEowYOTTj/z5lqM65hXRHnz/MhDOPJJbO3KKwu+TJ+ayFpvjmDAhc7GtDMRsE8nn2HsWtHlZLeTI8fGhZnNIYoTxkInjRDJLS5Z1I490XDvJh3059bE1KwF10OMqLi9bXpUs/5s=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";

    String rich_text_test_nonlink_unchanged_singleqoutes = "<img alt='' height='62' width='173' src='http://test//~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx' />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height='310' alt='koteyka' width='282' src='http://test//~/media/B386EDC10B3848969D36502E466FC359.ashx' /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href='http://google.com.ua' target='_blank'>somte link</a></p><p><a href='~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z'>&reg;</a></p><p class='SitecoreBulletedList'> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>'~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx'";
    String rich_text_test_changed_single_qoutes = "<img alt='' height='62' width='173' src='http://test//~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx' />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height='310' alt='koteyka' width='282' src='http://test//~/media/B386EDC10B3848969D36502E466FC359.ashx' /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href='http:/google.com.ua' target='_blank'>somte link</a></p><p><a href='~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z'>&reg;</a></p><p class='SitecoreBulletedList'> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>";
    String rich_text_test_changed = "<img alt=\"\" height=\"62\" width=\"173\" src=\"http://test//~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx\" />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height=\"310\" alt=\"koteyka\" width=\"282\" src=\"http://test//~/media/B386EDC10B3848969D36502E466FC359.ashx\" /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href=\"http://google.com.ua\" target=\"_blank\">somte link</a></p><p><a href=\"~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z\">&reg;</a></p><p class=\"SitecoreBulletedList\"> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>";
    String rich_text_test_notchanged_single_qoutes = "<img alt='' height='62' width='173' src='http://domain.name//~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx' />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height='310' alt='koteyka' width='282' src='http://domain.name//~/media/B386EDC10B3848969D36502E466FC359.ashx' /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href='http://google.com.ua' target='_blank'>somte link</a></p><p><a href='~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z'>something&reg;</a></p><p class='SitecoreBulletedList'> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>";
    String rich_text_not_changed = "<img alt=\"\" height=\"62\" width=\"173\" src=\"http://domain.name//~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx\" />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height=\"310\" alt=\"koteyka\" width=\"282\" src=\"http://domain.name//~/media/B386EDC10B3848969D36502E466FC359.ashx\" /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href=\"http://google.com.ua\" target=\"_blank\">somte link</a></p><p><a href=\"~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z\">something&reg;</a></p><p class=\"SitecoreBulletedList\"> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>";


    String _200_1_item_rich_text_test = "{ \"statusCode\": 200, \"result\": { \"totalCount\": 1, \"resultCount\": 1, \"items\": [ { \"Database\": \"web\", \"DisplayName\": \"Home\", \"HasChildren\": true, \"ID\": \"{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}\", \"Language\": \"en\", \"LongID\": \"/{11111111-1111-1111-1111-111111111111}/{0DE95AE4-41AB-4D01-9EB0-67441B7C2450}/{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}\", \"Path\": \"/sitecore/content/Home\", \"Template\": \"Sample/Sample Item\", \"Version\": 1, \"Fields\": { \"{75577384-3C97-45DA-A847-81B00500E250}\": { \"Name\": \"Text with single qoutes\", \"Type\": \"Rich Text\", \"Value\": \"<img alt='' height='62' width='173' src='~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx' />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height='310' alt='koteyka' width='282' src='~/media/B386EDC10B3848969D36502E466FC359.ashx' /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href='http:/google.com.ua' target='_blank'>somte link</a></p><p><a href='~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z'>&reg;</a></p><p class='SitecoreBulletedList'> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>\" }, \"{A60ACD61-A6DB-4182-8329-C957982CEC74}\": { \"Name\": \"Normal rich text\", \"Type\": \"Rich Text\", \"Value\": \"<img alt=\\\"\\\" height=\\\"62\\\" width=\\\"173\\\" src=\\\"~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx\\\" />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height=\\\"310\\\" alt=\\\"koteyka\\\" width=\\\"282\\\" src=\\\"~/media/B386EDC10B3848969D36502E466FC359.ashx\\\" /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href=\\\"http://google.com.ua\\\" target=\\\"_blank\\\">somte link</a></p><p><a href=\\\"~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z\\\">&reg;</a></p><p class=\\\"SitecoreBulletedList\\\"> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>\" }, \"{79FD03F6-3B27-4C92-86A4-2B8F9848BBC4}\": { \"Name\": \"Unchanged text\", \"Type\": \"Rich Text\", \"Value\": \"<img alt=\\\"\\\" height=\\\"62\\\" width=\\\"173\\\" src=\\\"http://domain.name//~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx\\\" />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height=\\\"310\\\" alt=\\\"koteyka\\\" width=\\\"282\\\" src=\\\"http://domain.name//~/media/B386EDC10B3848969D36502E466FC359.ashx\\\" /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href=\\\"http://google.com.ua\\\" target=\\\"_blank\\\">somte link</a></p><p><a href=\\\"~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z\\\">something&reg;</a></p><p class=\\\"SitecoreBulletedList\\\"> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>\" }, \"{A65D4786-E863-460F-AF8F-2C83CFF08B20}\": { \"Name\": \"Unchanged text Single qoutes\", \"Type\": \"Rich Text\", \"Value\": \"<img alt='' height='62' width='173' src='http://domain.name//~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx' />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height='310' alt='koteyka' width='282' src='http://domain.name//~/media/B386EDC10B3848969D36502E466FC359.ashx' /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href='http://google.com.ua' target='_blank'>somte link</a></p><p><a href='~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z'>something&reg;</a></p><p class='SitecoreBulletedList'> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>\" }, \"{A65D4786-E863-460F-AF8F-2C83CFF25B20}\": { \"Name\": \"With Non link\", \"Type\": \"Rich Text\", \"Value\": \"<img alt='' height='62' width='173' src='http://test//~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx' />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height='310' alt='koteyka' width='282' src='http://test//~/media/B386EDC10B3848969D36502E466FC359.ashx' /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href='http://google.com.ua' target='_blank'>somte link</a></p><p><a href='~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z'>&reg;</a></p><p class='SitecoreBulletedList'> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>'~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx'\" } } } ] } }";
    String _200_simple_response = "{\"statusCode\": 200, \"result\": { \"totalCount\": 4, \"resultCount\": 4, \"items\": [ { \"Database\": \"web\", \"DisplayName\": \"Home\", \"HasChildren\": true, \"ID\": \"{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}\", \"Language\": \"en\", \"LongID\": \"/{11111111-1111-1111-1111-111111111111}/{0DE95AE4-41AB-4D01-9EB0-67441B7C2450}/{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}\", \"Path\": \"/sitecore/content/Home\", \"Template\": \"Sample/Sample Item\", \"Version\": 1, \"Fields\": { \"{75577384-3C97-45DA-A847-81B00500E250}\": { \"Name\": \"Title\", \"Type\": \"text\", \"Value\": \"Sitecore\" }, \"{A60ACD61-A6DB-4182-8329-C957982CEC74}\": { \"Name\": \"Text\", \"Type\": \"Rich Text\", \"Value\": \"\\r\\n\\t\\t\\u003cp\\u003eWelcome to Sitecore\\u003c/p\\u003e\\r\\n\" } } }, { \"Database\": \"web\", \"DisplayName\": \"Allowed_Parent\", \"HasChildren\": true, \"ID\": \"{2075CBFF-C330-434D-9E1B-937782E0DE49}\", \"Language\": \"en\", \"LongID\": \"/{11111111-1111-1111-1111-111111111111}/{0DE95AE4-41AB-4D01-9EB0-67441B7C2450}/{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}/{2075CBFF-C330-434D-9E1B-937782E0DE49}\", \"Path\": \"/sitecore/content/Home/Allowed_Parent\", \"Template\": \"Sample/Sample Item\", \"Version\": 1, \"Fields\": { \"{75577384-3C97-45DA-A847-81B00500E250}\": { \"Name\": \"Title\", \"Type\": \"text\", \"Value\": \"Allowed_Parent\" }, \"{A60ACD61-A6DB-4182-8329-C957982CEC74}\": { \"Name\": \"Text\", \"Type\": \"Rich Text\", \"Value\": \"\" } } }, { \"Database\": \"web\", \"DisplayName\": \"Not_Allowed_Parent\", \"HasChildren\": true, \"ID\": \"{BD86BD6C-071A-4B5F-ABEA-71913A775DA7}\", \"Language\": \"en\", \"LongID\": \"/{11111111-1111-1111-1111-111111111111}/{0DE95AE4-41AB-4D01-9EB0-67441B7C2450}/{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}/{BD86BD6C-071A-4B5F-ABEA-71913A775DA7}\", \"Path\": \"/sitecore/content/Home/Not_Allowed_Parent\", \"Template\": \"Sample/Sample Item\", \"Version\": 1, \"Fields\": { \"{75577384-3C97-45DA-A847-81B00500E250}\": { \"Name\": \"Title\", \"Type\": \"text\", \"Value\": \"Not_Allowed_Parent\" }, \"{A60ACD61-A6DB-4182-8329-C957982CEC74}\": { \"Name\": \"Text\", \"Type\": \"Rich Text\", \"Value\": \"\" } } }, { \"Database\": \"web\", \"DisplayName\": \"Test Fields\", \"HasChildren\": false, \"ID\": \"{00CB2AC4-70DB-482C-85B4-B1F3A4CFE643}\", \"Language\": \"en\", \"LongID\": \"/{11111111-1111-1111-1111-111111111111}/{0DE95AE4-41AB-4D01-9EB0-67441B7C2450}/{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}/{00CB2AC4-70DB-482C-85B4-B1F3A4CFE643}\", \"Path\": \"/sitecore/content/Home/Test Fields\", \"Template\": \"Test Templates/Sample fields\", \"Version\": 1, \"Fields\": { \"{B6E6C30E-9009-4202-8B56-34F64B19223C}\": { \"Name\": \"Text\", \"Type\": \"Single-Line Text\", \"Value\": \"Text\" }, \"{47E89857-EA42-4CC6-9436-34560BDA73ED}\": { \"Name\": \"Image\", \"Type\": \"Image\", \"Value\": \"\\u003cimage mediaid=\\\"{4F20B519-D565-4472-B018-91CB6103C667}\\\" mediapath=\\\"/Images/test image\\\" src=\\\"~/media/4F20B519D5654472B01891CB6103C667.ashx\\\" /\\u003e\" }, \"{79FD03F6-3B27-4C92-86A4-2B8F9848BBC4}\": { \"Name\": \"CheckBoxField\", \"Type\": \"Checkbox\", \"Value\": \"1\" }, \"{2E6F8EF6-2D2B-4237-9D11-9B60C165702C}\": { \"Name\": \"DateField\", \"Type\": \"Date\", \"Value\": \"20120201T000000\" }, \"{B2383801-7FB5-4CF6-A174-522984CFCD0F}\": { \"Name\": \"DateTimeField\", \"Type\": \"Datetime\", \"Value\": \"20120201T120000\" }, \"{B5D28398-CEF0-485E-A5EE-548B2ECAFD0F}\": { \"Name\": \"MultiListField\", \"Type\": \"Multilist\", \"Value\": \"{2075CBFF-C330-434D-9E1B-937782E0DE49}\" }, \"{A07F6A5F-0225-4056-AD6C-BE153B0EF970}\": { \"Name\": \"TreeListField\", \"Type\": \"Treelist\", \"Value\": \"{2075CBFF-C330-434D-9E1B-937782E0DE49}|{00CB2AC4-70DB-482C-85B4-B1F3A4CFE643}\" }, \"{BA2B9E54-AD3B-4648-8400-AF7935FBF5A0}\": { \"Name\": \"CheckListField\", \"Type\": \"Checklist\", \"Value\": \"{2075CBFF-C330-434D-9E1B-937782E0DE49}\" }, \"{0A4E789C-33A4-4C61-9EFB-5785F79E5DF1}\": { \"Name\": \"DropLinkFieldEmpty\", \"Type\": \"Droplink\", \"Value\": \"\" }, \"{2C0DFF71-6FFC-41FC-8C13-B6B85AA308F3}\": { \"Name\": \"DropTreeFieldNormal\", \"Type\": \"Droptree\", \"Value\": \"{2075CBFF-C330-434D-9E1B-937782E0DE49}\" }, \"{2ECBA5DF-435A-42B7-8D79-50B8974035E2}\": { \"Name\": \"GeneralLinkFieldMediaNormal\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link description\\\" linktype=\\\"media\\\" url=\\\"/Images/test image\\\" title=\\\"Alternate Text\\\" target=\\\"\\\" id=\\\"{4F20B519-D565-4472-B018-91CB6103C667}\\\" /\\u003e\" }, \"{53E026A4-245F-4F6C-BA75-9DA7F9E01D38}\": { \"Name\": \"GeneralLinkFieldLinkEmpty\", \"Type\": \"General Link\", \"Value\": \"\" }, \"{BDB82506-3FCC-4581-ABB6-29CA32432FDC}\": { \"Name\": \"GeneralLinkFieldExtLinkInvalid\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link Description\\\" linktype=\\\"external\\\" url=\\\"http://abc!@test^_^\\\" anchor=\\\"\\\" title=\\\"Alternate Text\\\" target=\\\"\\\" /\\u003e\" }, \"{98A5C143-6D7A-49D4-939E-804414A191CC}\": { \"Name\": \"GeneralLinkFieldEmail\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link Description\\\" linktype=\\\"mailto\\\" url=\\\"mailto:rundueva@gmail.com\\\" anchor=\\\"\\\" title=\\\"Alternate Text\\\" /\\u003e\" }, \"{12ED400B-F2C5-4DED-943B-B8749355F75F}\": { \"Name\": \"GeneralLinkFieldJavascript\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link Description\\\" linktype=\\\"javascript\\\" url=\\\"javascript:document.write(\\u0027Test javascript field\\u0027)\\\" anchor=\\\"\\\" /\\u003e\" }, \"{4124EFC1-6D3D-4847-94EC-E65D16F8F5C9}\": { \"Name\": \"Normal Text\", \"Type\": \"Single-Line Text\", \"Value\": \"Normal Text\" }, \"{A65D4786-E863-460F-AF8F-2C83CFF08B20}\": { \"Name\": \"GeneralLinkFieldLinkNormal\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Description\\\" linktype=\\\"internal\\\" url=\\\"/Home/Allowed_Parent.aspx\\\" anchor=\\\"Anchor\\\" querystring=\\\"/*\\\" title=\\\"Alternate Text\\\" target=\\\"\\\" id=\\\"{2075CBFF-C330-434D-9E1B-937782E0DE49}\\\" /\\u003e\" }, \"{850F9731-5235-4C77-893C-13F1781ED9BF}\": { \"Name\": \"GeneralLinkFieldAnchor\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link Description\\\" linktype=\\\"anchor\\\" url=\\\"header1\\\" anchor=\\\"header1\\\" title=\\\"Alternate Text\\\" /\\u003e\" }, \"{9CA6AA92-C670-4B04-B12A-F20EBFAA04BD}\": { \"Name\": \"NotAllowedItem\", \"Type\": \"Droplist\", \"Value\": \"Not_Allowed_Parent\" } } } ] }}";
    String _200_1_item_20_fields = "{\"statusCode\": 200, \"result\": { \"totalCount\": 1, \"resultCount\": 1, \"items\": [ { \"Database\": \"web\", \"DisplayName\": \"Test Fields\", \"HasChildren\": false, \"ID\": \"{00CB2AC4-70DB-482C-85B4-B1F3A4CFE643}\", \"Language\": \"en\", \"LongID\": \"/{11111111-1111-1111-1111-111111111111}/{0DE95AE4-41AB-4D01-9EB0-67441B7C2450}/{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}/{00CB2AC4-70DB-482C-85B4-B1F3A4CFE643}\", \"Path\": \"/sitecore/content/Home/Test Fields\", \"Template\": \"Test Templates/Sample fields\", \"Version\": 1, \"Fields\": { \"{B6E6C30E-9009-4202-8B56-34F64B19223C}\": { \"Name\": \"Text\", \"Type\": \"Single-Line Text\", \"Value\": \"Text\" }, \"{47E89857-EA42-4CC6-9436-34560BDA73ED}\": { \"Name\": \"Image\", \"Type\": \"Image\", \"Value\": \"\\u003cimage mediaid=\\\"{4F20B519-D565-4472-B018-91CB6103C667}\\\" mediapath=\\\"/Images/test image\\\" src=\\\"~/media/4F20B519D5654472B01891CB6103C667.ashx\\\" /\\u003e\" }, \"{79FD03F6-3B27-4C92-86A4-2B8F9848BBC4}\": { \"Name\": \"CheckBoxField\", \"Type\": \"Checkbox\", \"Value\": \"1\" }, \"{2E6F8EF6-2D2B-4237-9D11-9B60C165702C}\": { \"Name\": \"DateField\", \"Type\": \"Date\", \"Value\": \"20120201T000000\" }, \"{B2383801-7FB5-4CF6-A174-522984CFCD0F}\": { \"Name\": \"DateTimeField\", \"Type\": \"Datetime\", \"Value\": \"20120201T120000\" }, \"{B5D28398-CEF0-485E-A5EE-548B2ECAFD0F}\": { \"Name\": \"MultiListField\", \"Type\": \"Multilist\", \"Value\": \"{2075CBFF-C330-434D-9E1B-937782E0DE49}\" }, \"{A07F6A5F-0225-4056-AD6C-BE153B0EF970}\": { \"Name\": \"TreeListField\", \"Type\": \"Treelist\", \"Value\": \"{2075CBFF-C330-434D-9E1B-937782E0DE49}|{00CB2AC4-70DB-482C-85B4-B1F3A4CFE643}\" }, \"{BA2B9E54-AD3B-4648-8400-AF7935FBF5A0}\": { \"Name\": \"CheckListField\", \"Type\": \"Checklist\", \"Value\": \"{2075CBFF-C330-434D-9E1B-937782E0DE49}\" }, \"{0A4E789C-33A4-4C61-9EFB-5785F79E5DF1}\": { \"Name\": \"DropLinkFieldEmpty\", \"Type\": \"Droplink\", \"Value\": \"\" }, \"{2C0DFF71-6FFC-41FC-8C13-B6B85AA308F3}\": { \"Name\": \"DropTreeFieldNormal\", \"Type\": \"Droptree\", \"Value\": \"{2075CBFF-C330-434D-9E1B-937782E0DE49}\" }, \"{2ECBA5DF-435A-42B7-8D79-50B8974035E2}\": { \"Name\": \"GeneralLinkFieldMediaNormal\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link description\\\" linktype=\\\"media\\\" url=\\\"/Images/test image\\\" title=\\\"Alternate Text\\\" target=\\\"\\\" id=\\\"{4F20B519-D565-4472-B018-91CB6103C667}\\\" /\\u003e\" }, \"{53E026A4-245F-4F6C-BA75-9DA7F9E01D38}\": { \"Name\": \"GeneralLinkFieldLinkEmpty\", \"Type\": \"General Link\", \"Value\": \"\" }, \"{BDB82506-3FCC-4581-ABB6-29CA32432FDC}\": { \"Name\": \"GeneralLinkFieldExtLinkInvalid\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link Description\\\" linktype=\\\"external\\\" url=\\\"http://abc!@test^_^\\\" anchor=\\\"\\\" title=\\\"Alternate Text\\\" target=\\\"\\\" /\\u003e\" }, \"{98A5C143-6D7A-49D4-939E-804414A191CC}\": { \"Name\": \"GeneralLinkFieldEmail\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link Description\\\" linktype=\\\"mailto\\\" url=\\\"mailto:rundueva@gmail.com\\\" anchor=\\\"\\\" title=\\\"Alternate Text\\\" /\\u003e\" }, \"{12ED400B-F2C5-4DED-943B-B8749355F75F}\": { \"Name\": \"GeneralLinkFieldJavascript\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link Description\\\" linktype=\\\"javascript\\\" url=\\\"javascript:document.write(\\u0027Test javascript field\\u0027)\\\" anchor=\\\"\\\" /\\u003e\" }, \"{4124EFC1-6D3D-4847-94EC-E65D16F8F5C9}\": { \"Name\": \"Normal Text\", \"Type\": \"Single-Line Text\", \"Value\": \"Normal Text\" }, \"{A65D4786-E863-460F-AF8F-2C83CFF08B20}\": { \"Name\": \"GeneralLinkFieldLinkNormal\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Description\\\" linktype=\\\"internal\\\" url=\\\"/Home/Allowed_Parent.aspx\\\" anchor=\\\"Anchor\\\" querystring=\\\"/*\\\" title=\\\"Alternate Text\\\" target=\\\"\\\" id=\\\"{2075CBFF-C330-434D-9E1B-937782E0DE49}\\\" /\\u003e\" }, \"{850F9731-5235-4C77-893C-13F1781ED9BF}\": { \"Name\": \"GeneralLinkFieldAnchor\", \"Type\": \"General Link\", \"Value\": \"\\u003clink text=\\\"Link Description\\\" linktype=\\\"anchor\\\" url=\\\"header1\\\" anchor=\\\"header1\\\" title=\\\"Alternate Text\\\" /\\u003e\" }, \"{9CA6AA92-C670-4B04-B12A-F20EBFAA04BD}\": { \"Name\": \"NotAllowedItem\", \"Type\": \"Droplist\", \"Value\": \"Not_Allowed_Parent\" }, \"{A60ACD61-A6DB-4182-8329-C957982CEC74}\": { \"Name\": \"Text\", \"Type\": \"Rich Text\", \"Value\": \"<img alt=\\\"\\\" height=\\\"62\\\" width=\\\"173\\\" src=\\\"~/media/72306E7F521B4F0CBDCB27D0E0341627.ashx\\\" />Sitecore Fast Query is translated to SQL queries that be executed by the database engine.<br />Sitecore Fast Query converts all the query conditions into SQL statements, and this places certain&nbsp;<br />limitations on the use of queries:<img height=\\\"310\\\" alt=\\\"koteyka\\\" width=\\\"282\\\" src=\\\"~/media/B386EDC10B3848969D36502E466FC359.ashx\\\" /><br /><p>&nbsp;Sitecore Fast Query only supports the following axeasdsadasd<a href=\\\"http://google.com.ua\\\" target=\\\"_blank\\\">somte link</a></p><p><a href=\\\"~/link.aspx?_id=AD2E79749A144F7A9CB0EB9C68CF2594&amp;_z=z\\\">Andriy Matkivskiy&reg;</a></p><p class=\\\"SitecoreBulletedList\\\"> </p><p>&middot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; are typically un<strong>available in a normal we</strong>bsite but you can access them through a set of HTML</p>\" } } } ] }}";
    String _401_access_not_granted = "{\"statusCode\":401,\"error\":{\"message\":\"Access to site is not granted.\"}}";
    String _200_delete_5_items = "{ \"statusCode\":200, \"result\":{ \"count\":5, \"itemIds\":[ \"{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}\", \"{2075CBFF-C330-434D-9E1B-937782E0DE49}\", \"{3DED3442-D502-4852-A5AA-8707CF66781A}\", \"{BD86BD6C-071A-4B5F-ABEA-71913A775DA7}\", \"{00CB2AC4-70DB-482C-85B4-B1F3A4CFE643}\" ] } }";

    interface Content {
        interface Home {
            String PATH = "/sitecore/content/Home";
            String ID = "{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}";
            interface Android {
                String PATH = "/sitecore/content/Home/Android";
                interface StaticItem {
                    String PATH = "/sitecore/content/Home/Android/Static/Test item 1";
                }
            }
            interface AllowedParent {
                String PATH = "/sitecore/content/Home/Allowed_Parent";
                String NAME = "Allowed_Parent";
                String ID = "{2075CBFF-C330-434D-9E1B-937782E0DE49}";
                String LONGID = "/{11111111-1111-1111-1111-111111111111}/{0DE95AE4-41AB-4D01-9EB0-67441B7C2450}/{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}/{2075CBFF-C330-434D-9E1B-937782E0DE49}";
            }
            interface NotAllowedParent {
                String PATH = "/sitecore/content/Home/Not_Allowed_Parent";
            }
            interface TestFields {
                String PATH = "/sitecore/content/Home/Test Fields";
            }
            interface TestFieldsRichText {
                String PATH = "/sitecore/content/FieldsTest/RichText";
            }
        }
        interface LanguageItemVersions {
            String PATH = "/sitecore/content/Language Test/Language Item Versions";
            String ID = "{7272BE8E-8C4C-4F2A-8EC8-F04F512B04CB}";
        }

        interface MediaLibrary {
            interface FolderToUpload {
                String PATH = "/sitecore/media library/Test Data/Android";
                String ID = "{EA742886-0EAE-4852-B0F8-5D9161BE363B}";
            }

            interface TestImage {
                String ID = "{4F20B519-D565-4472-B018-91CB6103C667}";
            }
        }
    }

    interface Renderings {
        interface SampleRendering {
            String ID = "{493B3A83-0FA7-4484-8FC9-4680991CF743}";
        }
        interface MasterSampleRendering {
            String ID = "{5FAC342C-AC30-4F74-8B61-6C38B527CF32}";
        }
    }

    interface Templates {
        interface SampleItem {
            String PATH = "Sample/Sample Item";
            String ID = "{76036F5E-CBCE-46D1-AF0A-4143F9B557AA}";
        }
        interface SampleFields {
            String PATH = "Test Templates/Sample fields";
            String ID = "{5FC0D542-E27B-4E55-A1F0-702E959DCD6C}";
        }
    }

    interface Branches {
        interface SampleItem {
            String ID = "{6AAC5413-4E9C-4D06-8080-CF28D53D1CE7}";
        }
        interface MultipleItems {
            String ID = "{14416817-CDED-45AF-99BF-2DE9883B7AC3}";
        }
    }

    String SHELL_SITE = "/sitecore/shell";

    interface Server {
        interface Path {
            interface Anonymous {
                //String ALLOW = "http://ws-olp.dk.sitecore.net:60";
                //String ALLOW = "http://mobiledev1ua1.dk.sitecore.net:89";
                String ALLOW = "http://scmobileteam.cloudapp.net/";
                String DENY = "http://mobiledev1ua1.dk.sitecore.net:9999";
            }
        }
    }

    interface Users {
        interface Extranet {
            interface Admin {
                String USERNAME = "extranet\\adminex";
                String PASSWORD = "adminex";
            }

            interface Creator {
                String USERNAME = "extranet\\creatorex";
                String PASSWORD = "creatorex";
            }

            interface NotExistedUser {
                String USERNAME = "extranet\\notexisteduser";
                String PASSWORD = "notexisteduser";
            }

            interface UserWithoutReadAccess {
                String USERNAME = "extranet\\noreadaccess";
                String PASSWORD = "noreadaccess";
            }
        }

        interface Sitecore {
            interface Admin {
                String USERNAME = "sitecore\\admin";
                String PASSWORD = "b";
            }
            interface Creator {
                String USERNAME = "sitecore\\creator";
                String PASSWORD = "creator";
            }
            interface Nocreate {
                String USERNAME = "sitecore\\nocreate";
                String PASSWORD = "nocreate";
            }
        }
    }

    interface Files {
        interface Images {
            interface NormalImage {
                String URL = "http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png";
            }
            interface LargeImage {
                String URL = "https://dl.dropboxusercontent.com/s/s6e36fgj95a63gw/green_mineraly1%20%282%29.jpg";
            }
        }
    }
}
