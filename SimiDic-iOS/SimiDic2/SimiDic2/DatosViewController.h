//
//  DatosViewController.h
//  SimiDic2
//
//  Created by KetanoLab on 22/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DatosViewController : UIViewController
- (IBAction)volver:(id)sender;
- (IBAction)Favoritos:(id)sender;

@property (strong, nonatomic) IBOutlet UILabel *wordDictionary;
@property (nonatomic,strong) NSString *palabra;
@property (strong, nonatomic) IBOutlet UIWebView *aWebView;
@property (strong, nonatomic) NSString *meaning;
@property (strong, nonatomic) NSString *author;
@property (strong, nonatomic) NSString *tipo;
@property (strong, nonatomic) NSString *summary;
@property (strong, nonatomic) IBOutlet UILabel *labelTipo;
@property (strong, nonatomic) IBOutlet UILabel *labelAuthor;
@property (strong, nonatomic) NSMutableArray * datosFavoritos;
@property (strong, nonatomic) IBOutlet UIButton *botonEstrella;
@property (nonatomic) int index;
@end
