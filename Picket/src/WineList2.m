//
//  WineList2.m
//  Collection
//
//  Created by Jean Barré on 07/07/2014.
//  Copyright (c) 2014 Jean Barré. All rights reserved.
//
// A short implementation of the wines selected sorted by ratings


#import "WineList2.h"

@implementation wineList2

/*
 I : wineName Array
 O : wineRank Array, computing wine's name at index its rank
 */

-(NSMutableArray *)buildWineRank:(NSMutableDictionary *)dict {
    NSMutableArray *rankTable = [[NSMutableArray alloc] initWithCapacity:7];
    for (int j = 0; j < 7; j++) {
        NSMutableArray *rankj = [[NSMutableArray alloc] init];
        rankTable[j] = rankj;
    }
    
    for (id key in dict) {
        NSMutableArray *wine = [dict objectForKey:key];
        NSNumber *rankAsNumber = wine[1];
        NSInteger rank = [rankAsNumber intValue];
        if (rank < 7) {
            [rankTable[rank] addObject:key];
        }
    }
    return rankTable;
}

/*
 I : Array Integer
 O : Complementary array from the entry one
 */
-(NSMutableArray*)complementaryArrayOf:(NSMutableArray*)arr fromSize:(NSInteger)size {
    NSMutableArray *result = [[NSMutableArray alloc] init];
    
    int j = 0;
    for (int i = 0 ; i < size ; i++) {
        if (j == [arr count]) {
            [result addObject:[NSNumber numberWithInt:i]];
        }
        else {
            if ([arr[j] intValue] != i)
                [result addObject:[NSNumber numberWithInt:i]];
            else
                j++;
        }
    }
    return result;
}

/*
 I : Array
 O : Array of the index of the empty objects in the input Array
 */
-(NSMutableArray*)emptyRankIndex:(NSMutableArray *)arr {
    NSMutableArray* result = [[NSMutableArray alloc] init];
    NSInteger arrSize = arr.count;
    
    for (int i = 0; i < arrSize; i++) {
        if ([arr[i] count] == 0) {
            [result addObject:[NSNumber numberWithInt:i]];
        }
    }
    return  result;
}

/*
 I : Array1 Array2
 O : Array1 without objects at index the values in Array2
 */
-(void)removeArraySection:(NSMutableArray*)arr atIndex:(NSMutableArray*)indexArr {
    NSInteger indexArrSize = [indexArr count];
    
    for (int i = 0 ; i < indexArrSize ; i++) {
        [arr removeObjectAtIndex: [indexArr[i] intValue]-i];
    }
}




+(NSString*)setWineColor:(NSInteger)value {
    NSString *color = [[NSString alloc] init];
    if (value == 1) {
        color = @"Rouge";
    }
    else if (value == 2) {
        color = @"Blanc";
    }
    else {
        color = @"Rosé";
    }
    return color;
}


+(NSString *)splitString:(NSString *)inputString {
    
    int index = 1;
    NSMutableString* mutableInputString = [NSMutableString stringWithString:inputString];
    
    while (index < mutableInputString.length) {
        
        if ([[NSCharacterSet uppercaseLetterCharacterSet] characterIsMember:[mutableInputString characterAtIndex:index]]) {
            [mutableInputString insertString:@" " atIndex:index];
            index++;
        }
        index++;
    }
    
    return [NSString stringWithString:mutableInputString];
}

@end
