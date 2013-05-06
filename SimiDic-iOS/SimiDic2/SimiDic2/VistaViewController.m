//
//  VistaViewController.m
//  SimiDic2
//
//  Created by KetanoLab on 20/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import "VistaViewController.h"

@interface VistaViewController ()

@end

@implementation VistaViewController
@synthesize tablaFavoritos,datosFavoritos;

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
    NSLog(@"%@",datosFavoritos);
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    [self setTablaFavoritos:nil];
    [super viewDidUnload];
}

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


// Método necesario para devolver el número de secciones de la tabla, por lo general será siempre 1..
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}
// Necesario para indicar el número de filas de la tabla, esto suele ir ligado al tamaño de un array de elementos a mostrar..
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.datosFavoritos count];
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    // Normalmente recuperaremos del array, según la posicion de la fila..
    NSDictionary *datoString = [self.datosFavoritos objectAtIndex:indexPath.row];
    // Creamos la celda (o fila).
    UITableViewCell *cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"Cell"];
    // Y le establecemos el texto de nuestro dato a una de las filas.
    cell.textLabel.text = [datoString objectForKey:@"palabra"];
    cell.detailTextLabel.text = [datoString objectForKey:@"summary"];
    cell.imageView.image = [UIImage imageNamed:@"ic_menu_star.png"];
    // Y finalizamos devolviendo la celda
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // En este método haremos lo que creamos oportuno. Por lo general pasaremos a una vista detalle del elemento seleccionado. Haciendo uso de  UINavigationController que se puede ver en el correspondiente tutorial.
    NSDictionary *datoString = [self.datosFavoritos objectAtIndex:indexPath.row];
    DatosViewController *aux = [[DatosViewController alloc]init];
    aux.palabra = [datoString objectForKey:@"palabra"];
    aux.tipo = [datoString objectForKey:@"tipo"];
    aux.meaning = [datoString objectForKey:@"meaning"];
    aux.author = [datoString objectForKey:@"author"];
    [self presentModalViewController:aux animated:YES];
}
- (IBAction)volverMenu:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}

-(void)viewWillAppear:(BOOL)animated{
    NSLog(@"volviendo.......");
    [self leer];
    [self.tablaFavoritos reloadData];
}
@end
