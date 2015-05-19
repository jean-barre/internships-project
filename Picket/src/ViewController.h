//
//  ViewController.h
//  Stars
//
//  Created by Jean Barré on 23/06/2014.
//  Copyright (c) 2014 Jean Barré. All rights reserved.
//
// The View Controller gather the information of a wine, among them the professionnal rating
// Here you can set a new personnal rate

#import <UIKit/UIKit.h>
#import "TransparentView.h"
#import "RateView.h"

@interface ViewController : UIViewController <RateViewDelegate>

@property (weak, nonatomic) IBOutlet UIImageView *winePict;
@property (weak, nonatomic) IBOutlet UILabel *domaineLabel;
@property (weak, nonatomic) IBOutlet UILabel *appellationLabel;
@property (weak, nonatomic) IBOutlet UILabel *zoneLabel;
@property (weak, nonatomic) IBOutlet UILabel *colorLabel;
@property (weak, nonatomic) IBOutlet UILabel *yearLabel;


@property (weak, nonatomic) IBOutlet RateView *rateViewPro;
@property (weak, nonatomic) IBOutlet UILabel *proRate;

@property (strong, nonatomic) IBOutlet RateView *rateView;
@property (weak, nonatomic) IBOutlet UILabel *statusLabel;


@property (weak, nonatomic) IBOutlet UIButton *setButton;
@property (strong, nonatomic) IBOutlet TransparentView *transparentView;
- (IBAction)showTransparent:(id)sender;
@property (weak, nonatomic) IBOutlet UIButton *transparentButton;

@property NSString *wineRef;
@property float proValue;
@property float persoValue;
@property (weak, nonatomic) UIImage *picture;
@property NSString *appellation;
@property NSInteger year;
@property NSString *zone;
@property NSInteger color;

@end
