using System.Collections;
using System.Collections.Generic;
using UnityEngine;
/// <summary>
/// Moves the game object from one place to another
/// </summary>
public class Mover : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
       //Rigidbody2D rb2d = GetComponent<Rigidbody2D>(); //get object to start moving
       //rb2d.AddForce(new Vector2(1,0),ForceMode2D.Impulse);
       GetComponent<Rigidbody2D>().AddForce(new Vector2 (1,0),ForceMode2D.Impulse);
    }

    //// Update is called once per frame
    //void Update()
    //{
        
    //}
}
