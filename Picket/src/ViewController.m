//
//  ViewController.m
//  Stars
//
//  Created by Jean Barré on 23/06/2014.
//  Copyright (c) 2014 Jean Barré. All rights reserved.
//
// The View Controller gather the information of a wine, among them the professionnal rating
// Here you can set a new personnal rate

#import "WineList2.h"
#import "ViewController.h"
#import "ViewController2.h"
#import <QuartzCore/QuartzCore.h>

@implementation ViewController
@synthesize proValue;
@synthesize persoValue;
@synthesize picture;
@synthesize appellation;
@synthesize wineRef;
@synthesize color;
@synthesize year;
@synthesize zone;

- (void)viewDidLoad
{
    [super viewDidLoad];
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
    [self setNeedsStatusBarAppearanceUpdate];
    
    self.domaineLabel.text = [wineList2 splitString:wineRef];
    self.appellationLabel.text = appellation;
    self.zoneLabel.text = zone;
    self.colorLabel.text = [wineList2 setWineColor:color];
    self.yearLabel.text = [[NSNumber numberWithInt:self.year] stringValue];
    self.winePict.image = picture;
    self.winePict.layer.cornerRadius = self.winePict.frame.size.height /2;
    self.winePict.layer.masksToBounds = YES;
    self.winePict.layer.borderWidth = 0;
    
    self.rateViewPro.notSelectedImage = [UIImage imageNamed:@"kermit_empty.png"];
    self.rateViewPro.halfSelectedImage = [UIImage imageNamed:@"kermit_half.png"];
    self.rateViewPro.fullSelectedImage = [UIImage imageNamed:@"kermit_full.png"];
    self.rateViewPro.rating = self.proValue;
    self.rateViewPro.editable = NO;
    self.rateViewPro.maxRating = 5;
    self.rateViewPro.delegate = self;
    NSString *proValueTxt = [[[NSNumber numberWithInt:self.proValue] stringValue] stringByAppendingString:@"/5"];
    self.proRate.text = proValueTxt;
    
	self.rateView.notSelectedImage = [UIImage imageNamed:@"kermit_empty.png"];
    self.rateView.halfSelectedImage = [UIImage imageNamed:@"kermit_half.png"];
    self.rateView.fullSelectedImage = [UIImage imageNamed:@"kermit_full.png"];
    if (persoValue < 6) {
        self.rateView.rating = persoValue;
    }
    else
        self.rateView.rating = 0;
    
    self.rateView.editable = NO;
    self.rateView.maxRating = 5;
    self.rateView.delegate = self;
    
    // To make a color gradient
    CAGradientLayer *gradient = [CAGradientLayer layer];
    gradient.frame = self.rateView.bounds;
    gradient.colors = [NSArray arrayWithObjects:(id)[[UIColor blackColor] CGColor], (id)[[UIColor darkGrayColor] CGColor], nil];
    //[self.rateView.layer insertSublayer:gradient atIndex:0];
    //[self.rateViewPro.layer insertSublayer:gradient atIndex:0];
    
    NSString *persoLabel = [[NSString alloc] init];
    if (persoValue>5) {
        persoLabel = @"Not Rated";
    }
    else
        persoLabel = [[NSString stringWithFormat:@"%d", (int)persoValue] stringByAppendingString:@"/5"];
    self.statusLabel.text = persoLabel;
    self.setButton.layer.cornerRadius = 10;
    
    self.transparentView.rateView = self.rateView;
    self.transparentView.rateView.editable = YES;
    
}

- (void)rateView:(RateView *)rateView ratingDidChange:(float)rating {
    //self.statusLabel.text = [NSString stringWithFormat:@"Rating: %f", rating];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)unwindSetRate:(UIStoryboardSegue *)segue{
    if ([segue.sourceViewController isKindOfClass:[ViewController2 class]]){
        ViewController2 *setViewController = (ViewController2 *)segue.sourceViewController;
        self.rateView.rating = setViewController.rateView.rating;
        [self viewDidLoad];
        [self viewWillAppear:YES];
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if([segue.identifier isEqualToString:@"toSet"]){
        ViewController2 *viewController2 = (ViewController2 *)segue.destinationViewController;
        viewController2.value = self.rateView.rating;
        viewController2.picture2 = self.winePict.image;
        viewController2.wineNameString2 = self.domaineLabel.text;
    }
}

-(void)singleTapGestureCaptured:(UITapGestureRecognizer *)gesture
{
    if(_transparentView){
        [_transparentView removeFromSuperview];
        self.transparentButton.hidden = FALSE;
    }
}

- (IBAction)showTransparent:(id)sender {
    _transparentView=[[[NSBundle mainBundle] loadNibNamed:@"TransparentView" owner:self options:nil] objectAtIndex:0];
    _transparentView.layer.borderWidth=3.0f;
    _transparentView.frame=CGRectMake(0, 370.0f, _transparentView.frame.size.width, _transparentView.frame.size.height);
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(singleTapGestureCaptured:)];
    [_transparentView addGestureRecognizer:singleTap];
    self.transparentButton.hidden = TRUE;
    [self.view addSubview:_transparentView];
}

@end
