//
//  DiccionariosViewController.m
//  SimiDic2
//
//  Created by KetanoLab on 02/04/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import "DiccionariosViewController.h"

@interface DiccionariosViewController ()

@end

@implementation DiccionariosViewController
@synthesize scroll,infoDiccionario;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self leer];
    
    scroll = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height) ];
    scroll.pagingEnabled = YES;
    scroll.multipleTouchEnabled = YES;
    scroll.center = CGPointMake(scroll.center.x,scroll.center.y+49);
    
    [self cargarVistas];
    
    //edito el tamaño del contenido de mi scroll
    scroll.contentSize = CGSizeMake(self.view.frame.size.width * [infoDiccionario count], self.view.frame.size.width);
    //añado a mi vista el scroll
    [self.view addSubview:scroll];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)cargarVistas{
    for (int i = 0; i < [infoDiccionario count]; i++) {
        NSDictionary *aux = [[NSDictionary alloc]init];
        aux = [infoDiccionario objectAtIndex:i];
        
        CGRect rect = CGRectMake(self.view.frame.size.width * i, 0, self.view.frame.size.width, self.view.frame.size.height);
        UIView *vista = [[UIImageView alloc]initWithFrame:rect];
        UIImageView * imagen = [[UIImageView alloc]initWithFrame:CGRectMake(97, 91, 126, 117)];
        [imagen setImage:[UIImage imageNamed:@"img_dictionary.png"]];
        
        UILabel * titulo = [[UILabel alloc]initWithFrame:CGRectMake(0, 240, self.view.frame.size.width, 21)];
        [titulo setTextAlignment:NSTextAlignmentCenter];
        titulo.text = [aux objectForKey:@"nombreDicc"];
        
        UILabel * descripcion = [[UILabel alloc]initWithFrame:CGRectMake(0, 269, self.view.frame.size.width, 140)];
        [descripcion setTextAlignment:NSTextAlignmentCenter];
        [descripcion setNumberOfLines:0];
        [descripcion setFont:[UIFont fontWithName:@"Times New Roman" size:14]];
        [descripcion setTextColor:[UIColor grayColor]];
        descripcion.text = [aux objectForKey:@"descripcion"];
                                 
        [vista addSubview:imagen];
        [vista addSubview:titulo];
        [vista addSubview:descripcion];
        [scroll addSubview:vista];
    }
}
- (IBAction)Volver:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}
-(void)leer{
    
    self.infoDiccionario =[[NSMutableArray alloc]initWithContentsOfFile:[self ruta]];
    if (self.infoDiccionario==nil) {
        self.infoDiccionario = [[NSMutableArray alloc]init];
    }
    
}

// Metodo para guardar cambios al archivo Plist en disco.
-(void)guardar{
    [self.infoDiccionario writeToFile:[self ruta] atomically:YES];
}

// Funcion que retorna la ruta donde se almacena el archivo Plist.
-(NSString *)ruta{
    NSString *rutacarpeta =[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSAllDomainsMask, YES)objectAtIndex:0];
    return [rutacarpeta stringByAppendingPathComponent:@"Datos.plist"];
}
@end
