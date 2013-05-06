//
//  DiccionariosViewController.h
//  SimiDic2
//
//  Created by KetanoLab on 02/04/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DiccionariosViewController : UIViewController<UIImagePickerControllerDelegate, UINavigationControllerDelegate>
- (IBAction)Volver:(id)sender;
@property (nonatomic,strong) UIScrollView * scroll;
@property (nonatomic,strong) NSMutableArray *infoDiccionario;
@end
