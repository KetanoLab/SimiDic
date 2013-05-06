//
//  CreditosIIViewController.h
//  SimiDic2
//
//  Created by KetanoLab on 01/04/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CreditosIIViewController : UIViewController
@property (nonatomic,strong) NSString * cadenaEstado;
@property (strong, nonatomic) IBOutlet UIView *primeraVista;
@property (strong, nonatomic) IBOutlet UIView *segundaVista;
@property (strong, nonatomic) IBOutlet UIView *terceraVista;
@property (strong, nonatomic) IBOutlet UIView *cuartaVista;
@property (strong, nonatomic) IBOutlet UIView *quintaVista;
@property (strong, nonatomic) IBOutlet UIView *sextaVista;
@property (strong, nonatomic) IBOutlet UIView *septimaVista;
@property (strong, nonatomic) IBOutlet UIView *octavaVista;
@property (strong, nonatomic) IBOutlet UIView *novenaVista;
@property (strong, nonatomic) IBOutlet UIButton *volver;
- (IBAction)volverDiccionarios:(id)sender;
@end
