package com.rohan.stockapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StockappApplicationTests {

	//@Test
	//public void contextLoads() {
	//}
	
	@Test
	public void matchRegex() throws IOException {
		
		String fileLoc = "C:\\temp\\1outputBHP.txt";
		
		File f = new File(fileLoc);
		
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(fileLoc)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		content = content.substring(content.indexOf("root.App.main"));
		
		//System.out.println(content);
		
		//"industry":.*"recommendationTrend
		
		
		String priceBlob="hain\":\"futures-chain\",\"HistoricalDataTable\":\"qsp-historical\",\"Holders\":\"qsp-holders\",\"Holders.Insiders\":\"qsp-insiders\",\"Holders.InsiderTransaction\":\"qsp-insider-transaction\",\"Holders.InsiderRoster\":\"qsp-insider-roster\",\"Holders.Summary\":\"qsp-holdings\",\"IndexComponents\":\"top-components\",\"IndustryLanding\":\"industry-landing\",\"KeyStatistics\":\"qsp-key-stats\",\"LineWidthSelector\":\"line-width-selector\",\"MarketDataTable\":\"datautility\",\"MarketSummary\":\"mrkt-sum\",\"MarketSummaryItem\":\"mrkt-sum\",\"MustRead\":\"must-read\",\"NewsStreamMyQuoteNews\":\"my-quote-news-stream\",\"NewsSummary\":\"news-summary\",\"MyMarketNews\":\"my-quote-news\",\"MarketNews\":\"market-news\",\"OptionContracts\":\"qsp-options\",\"OptionContractsTable\":\"qsp-option-table\",\"OptionContract:ResultItem\":\"qsp-options\",\"OptOutBtn\":\"opt-out\",\"PortfolioBreadcrumb\":\"pf-breadcrumb\",\"PortfolioControls\":\"pf-controls\",\"PortfolioHeader\":\"portfolio-hdr\",\"PortfolioName\":\"pf-table\",\"PortfolioNewsStream\":\"pf-news\",\"PortfolioTable\":\"pf-table\",\"PortfolioToolbar\":\"toolbar\",\"PositionRow\":\"datautility\",\"PressRelease\":\"press-releases\",\"QSP.FeaturedNews\":\"qsp-featured-news\",\"QSPSubNav\":\"qsp-subnav\",\"QSP.News\":\"qsp-news\",\"QSP.Section\":\"qsp-section\",\"QSPAddToWatchlist\":\"qsp-hdr\",\"Quote\":\"qsp\",\"QuoteContainer\":\"qsp\",\"QuoteHeader\":\"quote-header\",\"QuoteNews\":\"quote-news\",\"QuotesNews\":\"quotes-news\",\"QuotePill\":\"quote-pill\",\"QuoteRelated\":\"qsp-related\",\"QuoteRelatedVideo\":\"related-video\",\"RecommendationsByPortfolio\":\"pf-recommended-symbols\",\"RecommendationsBySymbol\":\"recommended-symbols\",\"ScreenerLanding\":\"screener-landing\",\"ScreenerCriteria\":\"screener-criteria\",\"ScreenerFilter\":\"screener-filter\",\"ScreenerNewFilter\":\"screener-new-filter\",\"ScreenerResults\":\"screener-results\",\"ScreenerSaveModal\":\"screener-save-modal\",\"ScreenerShare\":\"screener-share\",\"SecFilings\":\"qsp-filings\",\"SymbolLookup\":\"autocomplete\",\"SymbolLookup:ResultItem\":\"autocomplete\",\"SymbolRemoval\":\"datautility\",\"TableHead\":\"datautility\",\"TableRow\":\"datautility\",\"TickerNews\":\"ticker-news\",\"Trumponomics\":\"trumponomics\",\"Virgo:ResultItem\":\"chart\",\"VideoPlayerWithLangSelector\":\"video-player\",\"YFinListTable\":\"yfin-list-table\"},\"enableConsentData\":true},\"pageConfig\":{\"headerOverride\":null},\"routeConfig\":{\"base\":{\"rapid\":{\"keys\":{\"pt\":\"utility\",\"pct\":\"qsp\",\"pstcat\":\":quoteType\",\"pg_name\":\":category\",\"rvt\":\":symbol\",\"ticker\":\":symbol\",\"ver\":\"ydotcom\"}}}},\"runtimeConfig\":{\"rapid\":{\"keys\":{\"rvt\":\"DDR.AX\",\"ticker\":\"DDR.AX\",\"pstcat\":\"equities\",\"pg_name\":\"summary\",\"ccode_st\":\"finance-frontpage\"}}}},\"VideoPlayerStore\":{\"_config\":{\"docking\":{\"enableOnScrollDown\":false,\"enableOnScrollUp\":false,\"fadeInAnimation\":true,\"position\":{\"left\":\"ref\",\"right\":0,\"bottom\":45},\"ref\":\".modal-open .render-target-modal .modalRight\",\"width\":300,\"height\":168.75,\"threshold\":60,\"enableOnMuted\":true,\"showInfoCard\":false},\"enableRestoreOnNavigate\":true,\"enableUndockOnNavigate\":true,\"refreshDockingOnNavigate\":true,\"totalInactivePlayers\":10,\"videoClickSrc\":[\"video-click\",\"startScreen\"]},\"_playerConfig\":{}},\"QuoteAutoCompleteStore\":{\"clear\":true},\"FlyoutStore\":{},\"NavrailStore\":{\"showNavrail\":false,\"navTitle\":\"finance\",\"navSections\":\"\",\"currentUrl\":\"\\u002Fquote\\u002FDDR.AX\",\"pageType\":{},\"navSectionsDisplayTitle\":{},\"site\":\"finance\"},\"StreamDataStore\":{\"quoteData\":{\"DDR.AX\":{\"sourceInterval\":20,\"regularMarketOpen\":{\"raw\":3.01,\"fmt\":\"3.01\"},\"exchange\":\"ASX\",\"regularMarketTime\":{\"raw\":1538719822,\"fmt\":\"4:10PM AEST\"},\"fiftyTwoWeekRange\":{\"raw\":\"2.55 - 3.24\",\"fmt\":\"2.55 - 3.24\"},\"sharesOutstanding\":{\"raw\":160644992,\"fmt\":\"160.645M\",\"longFmt\":\"160,644,992\"},\"regularMarketDayHigh\":{\"raw\":3.03,\"fmt\":\"3.03\"},\"shortName\":\"DICKERDATA FPO\",\"longName\":\"Dicker Data Limited\",\"exchangeTimezoneName\":\"Australia\\u002FSydney\",\"regularMarketChange\":{\"raw\":0.029999971,\"fmt\":\"0.03\"},\"regularMarketPreviousClose\":{\"raw\":3,\"fmt\":\"3.00\"},\"fiftyTwoWeekHighChange\":{\"raw\":-0.21000004,\"fmt\":\"-0.21\"},\"exchangeTimezoneShortName\":\"AEST\",\"fiftyTwoWeekLowChange\":{\"raw\":0.48000002,\"fmt\":\"0.48\"},\"exchangeDataDelayedBy\":0,\"regularMarketDayLow\":{\"raw\":2.98,\"fmt\":\"2.98\"},\"priceHint\":2,\"currency\":\"AUD\",\"regularMarketPrice\":{\"raw\":3.03,\"fmt\":\"3.03\"},\"regularMarketVolume\":{\"raw\":65377,\"fmt\":\"65,377\",\"longFmt\":\"65,377\"},\"isLoading\":false,\"gmtOffSetMilliseconds\":36000000,\"region\":\"US\",\"marketState\":\"CLOSED\",\"marketCap\":{\"raw\":486754336,\"fmt\":\"486.754M\",\"longFmt\":\"486,754,336\"},\"quoteType\":\"EQUITY\",\"invalid\":false,\"symbol\":\"DDR.AX\",\"language\":\"en-US\",\"fiftyTwoWeekLowChangePercent\":{\"raw\":0.1882353,\"fmt\":\"18.82%\"},\"regularMarketDayRange\":{\"raw\":\"2.98 - 3.03\",\"fmt\":\"2.98 - 3.03\"},\"messageBoardId\":\"finmb_62196739\",\"fiftyTwoWeekHigh\":{\"raw\":3.24,\"fmt\":\"3.24\"},\"fiftyTwoWeekHighChangePercent\":{\"raw\":-0.06481483,\"fmt\":\"-6.48%\"},\"uuid\":\"290be918-6419-3ea8-9900-043378364e0c\",\"market\":\"au_market\",\"fiftyTwoWeekLow\":{\"raw\":2.55,\"fmt\":\"2.55\"},\"regularMarketChangePercent\":{\"raw\":0.99999905,\"fmt\":\"1.00%\"},\"fullExchangeName\":\"ASX\",\"tradeable\":false},\"ALU.AX\":{\"sourceInterval\":20,\"regularMarketOpen\":{\"raw\":25.85,\"fmt\":\"25.85\"},\"exchange\":\"ASX\",\"regularMarketTime\":{\"raw\":1538719817,\"fmt\":\"4:10PM AEST\"},\"fiftyTwoWeekRange\":{\"raw\":\"10.65 - 30.51\",\"fmt\":\"10.65 - 30.51\"},\"sharesOutstanding\":{\"raw\":130382000,\"fmt\":\"130.382M\",\"longFmt\":\"130,382,000\"},\"regularMarketDayHigh\":{\"raw\":25.965,\"fmt\":\"25.97\"},\"shortName\":\"ALTIUM LTD FPO\",\"longName\":\"Altium Limited\",\"exchangeTimezoneName\":\"Australia\\u002FSydney\",\"regularMarketChange\":{\"raw\":-0.42000008,\"fmt\":\"-0.42\"},\"regularMarketPreviousClose\":{\"raw\":26.11,\"fmt\":\"26.11\"},\"fiftyTwoWeekHighChange\":{\"raw\":-4.8199997,\"fmt\":\"-4.82\"},\"exchangeTimezoneShortName\":\"AEST\",\"fiftyTwoWeekLowChange\":{\"raw\":15.040001,\"fmt\":\"15.04\"},\"exchangeDataDelayedBy\":0,\"regularMarketDayLow\":{\"raw\":25.56,\"fmt\":\"25.56\"},\"priceHint\":2,\"currency\":\"AUD\",\"regularMarketPrice\":{\"raw\":25.69,\"fmt\":\"25.69\"},\"regularMarketVolume\":{\"raw\":441267,\"fmt\":\"441,267\",\"longFmt\":\"441,267\"},\"isLoading\":false,\"gmtOffSetMilliseconds\":36000000,\"region\":\"US\",\"marketState\":\"CLOSED\",\"marketCap\":{\"raw\":3349513728,\"fmt\":\"3.35B\",\"longFmt\":\"3,349,513,728\"},\"quoteType\":\"EQUITY\",\"invalid\":false,\"symbol\":\"ALU.AX\",\"language\":\"en-US\",\"fiftyTwoWeekLowChangePercent\":{\"raw\":1.4122066,\"fmt\":\"141.22%\"},\"regularMarketDayRange\":{\"raw\":\"25.56 - 25.965\",\"fmt\":\"25.56 - 25.97\"},\"messageBoardId\":\"finmb_4509158\",\"fiftyTwoWeekHigh\":{\"raw\":30.51,\"fmt\":\"30.51\"},\"fiftyTwoWeekHighChangePercent\":{\"raw\":-0.15798098,\"fmt\":\"-15.80%\"},\"uuid\":\"32661cc7-3df8-34cd-8602-53484116e2e3\",\"market\":\"au_market\",\"fiftyTwoWeekLow\":{\"raw\":10.65,\"fmt\":\"10.65\"},\"regularMarketChangePercent\":{\"raw\":-1.6085794,\"fmt\":\"-1.61%\"},\"fullExchangeName\":\"ASX\",\"tradeable\":false},\"APX.AX\":{\"sourceInterval\":20,\"regularMarketOpen\":{\"raw\":13.8,\"fmt\":\"13.80\"},\"exchange\":\"ASX\",\"regularMarketTime\":{\"raw\":1538719817,\"fmt\":\"4:10PM AEST\"},\"fiftyTwoWeekRange\":{\"raw\":\"4.84 - 16.0\",\"fmt\":\"4.84 - 16.00\"},\"sharesOutstanding\":{\"raw\":106476000,\"fmt\":\"106.476M\",\"longFmt\":\"106,476,000\"},\"regularMarketDayHigh\":{\"raw\":13.8,\"fmt\":\"13.80\"},\"shortName\":\"APPEN FPO\",\"longName\":\"Appen Limited\",\"exchangeTimezoneName\":\"Australia\\u002FSydney\",\"regularMarketChange\":{\"raw\":-0.57000065,\"fmt\":\"-0.57\"},\"regularMarketPreviousClose\":{\"raw\":14.02,\"fmt\":\"14.02\"},\"fiftyTwoWeekHighChange\":{\"raw\":-2.5500002,\"fmt\":\"-2.55\"},\"exchangeTimezoneShortName\":\"AEST\",\"fiftyTwoWeekLowChange\":{\"raw\":8.61,\"fmt\":\"8.61\"},\"exchangeDataDelayedBy\":0,\"regularMarketDayLow\":{\"raw\":13.32,\"fmt\":\"13.32\"},\"priceHint\":2,\"currency\":\"AUD\",\"regularMarketPrice\":{\"raw\":13.45,\"fmt\":\"13.45\"},\"regularMarketVolume\":{\"raw\":646621,\"fmt\":\"646,621\",\"longFmt\":\"646,621\"},\"isLoading\":false,\"gmtOffSetMilliseconds\":36000000,\"region\":\"US\",\"marketState\":\"CLOSED\",\"marketCap\":{\"raw\":1432102144,\"fmt\":\"1.432B\",\"longFmt\":\"1,432,102,144\"},\"quoteType\":\"EQUITY\",\"invalid\":false,\"symbol\":\"APX.AX\",\"language\":\"en-US\",\"fiftyTwoWeekLowChangePercent\":{\"raw\":1.7789255,\"fmt\":\"177.89%\"},\"regularMarketDayRange\":{\"raw\":\"13.32 - 13.8\",\"fmt\":\"13.32 - 13.80\"},\"messageBoardId\":\"finmb_278887416\",\"fiftyTwoWeekHigh\":{\"raw\":16,\"fmt\":\"16.00\"},\"fiftyTwoWeekHighChangePercent\":{\"raw\":-0.15937501,\"fmt\":\"-15.94%\"},\"uuid\":\"ac41ec84-e082-39a2-8916-8d9b3a25e0be\",\"market\":\"au_market\",\"fiftyTwoWeekLow\":{\"raw\":4.84,\"fmt\":\"4.84\"},\"regularMarketChangePercent\":{\"raw\":-4.065625,\"fmt\":\"-4.07%\"},\"fullExchangeName\":\"ASX\",\"tradeable\":false},\"NXT.AX\":{\"sourceInterval\":20,\"regularMarketOpen\":{\"raw\":6.3,\"fmt\":\"6.30\"},\"exchange\":\"ASX\",\"regularMarketTime\":{\"raw\":1538719829,\"fmt\":\"4:10PM AEST\"},\"fiftyTwoWeekRange\":{\"raw\":\"4.54 - 8.19\",\"fmt\":\"4.54 - 8.19\"},\"sharesOutstanding\":{\"raw\":343655008,\"fmt\":\"343.655M\",\"longFmt\":\"343,655,008\"},\"regularMarketDayHigh\":{\"raw\":6.32,\"fmt\":\"6.32\"},\"shortName\":\"NEXTDC FPO\",\"longName\":\"NEXTDC Limited\",\"exchangeTimezoneName\":\"Australia\\u002FSydney\",\"regularMarketChange\":{\"raw\":-0.1800003,\"fmt\":\"-0.18\"},\"regularMarketPreviousClose\":{\"raw\":6.42,\"fmt\":\"6.42\"},\"fiftyTwoWeekHighChange\":{\"raw\":-1.9499998,\"fmt\":\"-1.95\"},\"exchangeTimezoneShortName\":\"AEST\",\"fiftyTwoWeekLowChange\":{\"raw\":1.6999998,\"fmt\":\"1.70\"},\"exchangeDataDelayedBy\":0,\"regularMarketDayLow\":{\"raw\":6.19,\"fmt\":\"6.19\"},\"priceHint\":2,\"currency\":\"AUD\",\"regularMarketPrice\":{\"raw\":6.24,\"fmt\":\"6.24\"},\"regularMarketVolume\":{\"raw\":3029809,\"fmt\":\"3.03M\",\"longFmt\":\"3,029,809\"},\"isLoading\":false,\"gmtOffSetMilliseconds\"";
		
		priceBlob=content;
		/*
		System.out.println(blob+"111");
		System.out.println("5");
		System.out.println("4");
		System.out.println("3");
		System.out.println("2");
		System.out.println(blob);
		System.out.println("1");
		*/
		//     \"currency\":\"AUD\",\"regularMarketPrice\":{\"raw\":3.03,\"fmt\":\"3.03\"},
		
		//Pattern pattern = Pattern.compile(".*BHP\\.AX.*regularMarketPrice.*fmt\":\"(\\d+\\.\\d+).*regularMarketVolume.*?",Pattern.DOTALL | Pattern.MULTILINE);
		//Pattern pattern = Pattern.compile(".*?BHP\\.AX.*?regularMarketPrice.*?fmt\":\"(\\d+\\.\\d+)\"},\"regularMarketVolume.*?",Pattern.DOTALL | Pattern.MULTILINE);
		//Pattern pattern2 = Pattern.compile("^BHP.AX.*(\\d+\\.\\d+).*?earnings$",Pattern.DOTALL | Pattern.MULTILINE);
		Pattern pattern1 = Pattern.compile(".*?BHP\\.AX.*?regularMarketPrice.*?fmt\":\"(\\d+\\.\\d+)\"}.*",Pattern.DOTALL | Pattern.MULTILINE);
		
		//BHP.AX.*35.50.*?earnings
		
		Matcher matcher = pattern1.matcher(priceBlob);
		
		boolean matches = matcher.matches();
		
		if (matches) {			
			System.out.println(matches);			
			System.out.println(matcher.group(1));
			
		} else System.out.println("Nope");
		
		// Now try to match the industry
		
		
		//String industryBlob = "2006 and is headquartered in Melbourne, Australia.\",\"city\":\"Melbourne\",\"state\":\"VIC\",\"country\":\"Australia\",\"companyOfficers\":[],\"website\":\"http:\\u002F\\u002Fwww.kogancorporate.com\",\"maxAge\":86400,\"address1\":\"139 Gladstone Street\",\"industry\":\"Specialty Retail\",\"address2\":\"South Melbourne\"},\"recommendationTrend\":{\"trend\":[{\"period\":\"0m\",\"strongBuy\":0,\"buy\":0,";
		String industryBlob = "u\",\"maxAge\":86400,\"address1\":\"275 Kent Street\",\"fax\":\"61 2 8253 4128\",\"industry\":\"Banks - Global\"},\"recommendationTrend\":{\"trend\":[{\"period\":\"0m\",\"strongBuy\":0,\"bu";
		industryBlob = new String ( Files.readAllBytes( Paths.get("\\temp\\outputRHC.txt") ) );
		//System.out.println(industryBlob.length());
		boolean tru = industryBlob.contains("edical");
		
		//Pattern industryPattern1 = Pattern.compile(".*?\"industry\":\"(.*)\"[,]?.*recommendationTrend\".*?",Pattern.DOTALL | Pattern.MULTILINE);
		Pattern industryPattern1 = Pattern.compile(".*\"industry\":\"(.*)\"[,]?.*recommendationTrend\".*",Pattern.DOTALL | Pattern.MULTILINE);
		//Pattern industryPattern1 = Pattern.compile(".*RHC.*",Pattern.DOTALL | Pattern.MULTILINE);
		
		Matcher matcher1 = industryPattern1.matcher(industryBlob);
		long before = System.currentTimeMillis();
		boolean matches1 = matcher1.matches();
		long duration = System.currentTimeMillis() - before;
		System.out.println(duration);
		if (matches1) {			
			System.out.println(matches1);		
			System.out.println(StringUtils.substringBefore(matcher1.group(1), "\""));
		} else System.out.println("Nope");

	}

}
