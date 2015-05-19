//
//  WineList2.h
//  Collection
//
//  Created by Jean Barré on 07/07/2014.
//  Copyright (c) 2014 Jean Barré. All rights reserved.
//

#import <Foundation/Foundation.h>

@class wineList2;

@interface wineList2 : NSObject

@property NSMutableDictionary *wineName;
@property NSMutableArray *wineRank;
@property NSMutableArray *emptySections;


-(NSMutableArray*)buildWineRank:(NSMutableDictionary*)arr;
-(NSMutableArray*)emptyRankIndex:(NSMutableArray*)arr;
-(void)removeArraySection:(NSMutableArray*)arr atIndex:(NSMutableArray*)indexArr;
-(NSMutableArray*)complementaryArrayOf:(NSMutableArray*)arr fromSize:(NSInteger)size;
+(NSString*)setWineColor:(NSInteger)value;
+(NSString *)splitString:(NSString *)inputString;

@end

