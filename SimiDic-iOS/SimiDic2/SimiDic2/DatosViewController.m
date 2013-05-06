//
//  DatosViewController.m
//  SimiDic2
//
//  Created by KetanoLab on 22/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import "DatosViewController.h"

@interface DatosViewController ()
@end

@implementation DatosViewController
@synthesize wordDictionary,palabra,aWebView,meaning,tipo,labelTipo,labelAuthor,author,datosFavoritos,botonEstrella,index,summary;

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
    // Do any additional setup after loading the view from its nib.
    [self leer];
    for (int i = 0; i < [self.datosFavoritos count]; i++) {
        NSDictionary *aux = [[NSDictionary alloc]init];
        aux = [datosFavoritos objectAtIndex:i];
        if ([[aux objectForKey:@"meaning"]isEqualToString:meaning]) {
            [botonEstrella setImage:[UIImage imageNamed:@"ic_action_star_ok.png"] forState:UIControlStateNormal];
            [botonEstrella setSelected:YES];
            self.index = i;
            break;
        }
        
    }
    labelTipo.text = tipo;
    wordDictionary.text = palabra;
    [aWebView loadHTMLString:meaning baseURL:nil];
    labelAuthor.text = author;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)volver:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}

- (IBAction)Favoritos:(id)sender{
    
    if ([sender isSelected]) {
        [sender setImage:[UIImage imageNamed:@"ic_action_star.png"] forState:UIControlStateNormal];
        [sender setSelected:NO];
        [self.datosFavoritos removeObjectAtIndex:index];
        [self guardar];
    }else {
        [sender setImage:[UIImage imageNamed:@"ic_action_star_ok.png"] forState:UIControlStateSelected];
        [sender setSelected:YES];
        NSLog(@"Voy primero");
        NSDictionary *aux = [[NSDictionary alloc]initWithObjectsAndKeys:self.tipo,@"tipo",self.meaning,@"meaning",self.palabra,@"palabra",self.author,@"author",self.summary,@"summary", nil];
        [self.datosFavoritos addObject:aux];
        [self guardar];
    }
    
}
// Metodos necesarios para la lectura y escritura de datos ademas de obtener la ruta de el archivo Plist.
// Metodo de lectura archivo Plist en caso de no existir se crea solo una vez.
-(void)leer{
    
    self.datosFavoritos =[[NSMutableArray alloc]initWithContentsOfFile:[self ruta]];
    if (self.datosFavoritos==nil) {
        self.datosFavoritos = [[NSMutableArray alloc]init];
    }
    
}

// Metodo para guardar cambios al archivo Plist en disco.
-(void)guardar{
    [self.datosFavoritos writeToFile:[self ruta] atomically:YES];
}

// Funcion que retorna la ruta donde se almacena el archivo Plist.
-(NSString *)ruta{
    NSString *rutacarpeta =[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSAllDomainsMask, YES)objectAtIndex:0];
    return [rutacarpeta stringByAppendingPathComponent:@"Favoritos.plist"];
}

- (void)viewDidUnload {
    [self setWordDictionary:nil];
    [self setAWebView:nil];
    [self setLabelTipo:nil];
    [self setLabelAuthor:nil];
    [self setBotonEstrella:nil];
    [super viewDidUnload];
}
@end
