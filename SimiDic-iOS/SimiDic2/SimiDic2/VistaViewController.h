//
//  VistaViewController.h
//  SimiDic2
//
//  Created by KetanoLab on 20/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DatosViewController.h"
@interface VistaViewController : UIViewController
@property (strong, nonatomic) IBOutlet UITableView *tablaFavoritos;
@property (nonatomic,strong) NSMutableArray *datosFavoritos;
- (IBAction)volverMenu:(id)sender;
@end
