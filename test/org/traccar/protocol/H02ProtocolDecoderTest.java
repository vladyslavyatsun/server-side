package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class H02ProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        H02ProtocolDecoder decoder = new H02ProtocolDecoder(new H02Protocol());

        verifyPosition(decoder, buffer(
                "*HQ,4210051415,V1,164549,A,0956.3869,N,08406.7068,W,000.00,000,221215,FFFFFBFF,712,01,0,0,6#"),
                position("2015-12-22 16:45:49.000", true, 9.93978, -84.11178));

        verifyNothing(decoder, buffer(
                "*HQ,1451316451,NBR,112315,724,10,2,2,215,2135,123,215,2131,121,011215,FFFFFFFF#"));

        verifyPosition(decoder, buffer(
                "*HQ,1451316485,V1,121557,A,-23-3.3408,S,-48-2.8926,W,0.1,158,241115,FFFFFFFF#"));

        verifyPosition(decoder, buffer(
                "*HQ,1451316485,V1,121557,A,-23-35.3408,S,-48-2.8926,W,0.1,158,241115,FFFFFFFF#"));
        
        verifyPosition(decoder, buffer(
                "*HQ,355488020119695,V1,050418,,2827.61232,N,07703.84822,E,0.00,0,031015,FFFEFBFF#"),
                position("2015-10-03 05:04:18.000", false, 28.46021, 77.06414));

        verifyPosition(decoder, buffer(
                "*HQ,1451316409,V1,030149,A,-23-29.0095,S,-46-51.5852,W,2.4,065,070315,FFFFFFFF#"),
                position("2015-03-07 03:01:49.000", true, -23.48349, -46.85975));

        verifyNothing(decoder, buffer(
                "*HQ,353588020068342,V1,000000,V,0.0000,0,0.0000,0,0.00,0.00,000000,ffffffff,000106,000002,000203,004c87,16#"));

        verifyPosition(decoder, buffer(
                "*HQ,3800008786,V1,062507,V,3048.2437,N,03058.5617,E,000.00,000,250413,FFFFFBFF#"));
        
        verifyPosition(decoder, buffer(
                "*HQ,4300256455,V1,111817,A,1935.5128,N,04656.3243,E,0.00,100,170913,FFE7FBFF#"));

        verifyPosition(decoder, buffer(
                "*HQ,123456789012345,V1,155850,A,5214.5346,N,2117.4683,E,0.00,270.90,131012,ffffffff,000000,000000,000000,000000#"));
        
        verifyPosition(decoder, buffer(
                "*HQ,353588010001689,V1,221116,A,1548.8220,S,4753.1679,W,0.00,0.00,300413,ffffffff,0002d4,000004,0001cd,000047#"));

        verifyPosition(decoder, buffer(
                "*HQ,354188045498669,V1,195200,A,701.8915,S,3450.3399,W,0.00,205.70,050213,ffffffff,000243,000000,000000#"));
        
        verifyPosition(decoder, buffer(
                "*HQ,2705171109,V1,213324,A,5002.5849,N,01433.7822,E,0.00,000,140613,FFFFFFFF#"));
        
        verifyPosition(decoder, buffer(
                "*TH,2020916012,V1,050316,A,2212.8745,N,11346.6574,E,14.28,028,220902,FFFFFBFF#"));
        
        verifyPosition(decoder, buffer(
                "*TH,2020916012,V4,S17,130305,050316,A,2212.8745,N,11346.6574,E,14.28,028,220902,FFFFFBFF#"));
        
        verifyPosition(decoder, buffer(
                "*TH,2020916012,V4,S14,100,10,1,3,130305,050316,A,2212.8745,N,11346.6574,E,14.28,028,220902,FFFFFBFF#"));
        
        verifyPosition(decoder, buffer(
                "*TH,2020916012,V4,S20,ERROR,130305,050316,A,2212.8745,N,11346.6574,E,14.28,028,220902,FFFFFBFF#"));
        
        verifyPosition(decoder, buffer(
                "*TH,2020916012,V4,S20,DONE,130305,050316,A,2212.8745,N,11346.6574,E,14.28,028,220902,F7FFFBFF#"));
        
        verifyPosition(decoder, buffer(
                "*TH,2020916012,V4,R8,ERROR,130305,050316,A,2212.8745,N,11346.6574,E,14.28,028,220902,FFFFFBFF#"));
        
        verifyPosition(decoder, buffer(
                "*TH,2020916012,V4,S23,165.165.33.250:8800,130305,050316,A,2212.8745,N,11346.6574,E,14.28,028,220902,FFFFFBFF#"));
        
        verifyPosition(decoder, buffer(
                "*TH,2020916012,V4,S24,thit.gd,130305,050316,A,2212.8745,N,11346.6574,E,14.28,028,220902,FFFFFBFF#"));
        
        verifyPosition(decoder, buffer(
                "*TH,2020916012,V4,S1,OK,pass_word,130305,050316,A,2212.8745,N,11346.6574,E,14.28,028,220902,FFFFFBFD#"));
        
        verifyPosition(decoder, buffer(
                "*HQ,353588020068342,V1,062840,A,5241.1249,N,954.9490,E,0.00,0.00,231013,ffffffff,000106,000002,000203,004c87,24#"));

        verifyPosition(decoder, buffer(
                "*HQ,353505220903211,V1,075228,A,5227.5039,N,01032.8443,E,0.00,0,231013,FFFBFFFF,106,14, 201,2173#"));

        verifyPosition(decoder, buffer(
                "*HQ,353505220903211,V1,140817,A,5239.3538,N,01003.5292,E,21.03,312,221013,FFFBFFFF,106,14, 203,1cd#"));
        
        verifyPosition(decoder, buffer(
                "*HQ,356823035368767,V1,083618,A,0955.6392,N,07809.0796,E,0.00,0,070414,FFFBFFFF,194,3b5,  71,c9a9#"));

        verifyNothing(decoder, buffer(
                "*HQ,8401016597,BASE,152609,0,0,0,0,211014,FFFFFFFF#"));

        verifyPosition(decoder, binary(
                "24410600082621532131081504419390060740418306000000fffffbfdff0015060000002c02dc0c000000001f"),
                position("2015-08-31 21:53:21.000", true, 4.69898, -74.06971));

        verifyPosition(decoder, binary(
                "2427051711092133391406135002584900014337822e000000ffffffffff0000"));

        verifyPosition(decoder, binary(
                "2427051711092134091406135002584900014337822e000000ffffffffff0000"));

        verifyPosition(decoder, binary(
                "2410307310010503162209022212874500113466574C014028fffffbffff0000"));

        verifyPosition(decoder, binary(
                "2441090013450831250401145047888000008554650e000000fffff9ffff001006000000000106020299109c01"));

        verifyPosition(decoder, binary(
                "24270517030820321418041423307879000463213792000056fffff9ffff0000"));

        verifyPosition(decoder, binary(
                "2441091144271222470112142233983006114026520E000000FFFFFBFFFF0014060000000001CC00262B0F170A"));
        
        verifyPosition(decoder, binary(
                "24971305007205201916101533335008000073206976000000effffbffff000252776566060000000000000000000049"));

    }

}
