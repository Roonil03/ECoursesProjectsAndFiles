using JetBrains.Annotations;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SizeChanging : MonoBehaviour
{
    // Start is called before the first frame update
    static int res = 4;
    void Start()
    {
        //public int res = 4;
        Vector3 newScale = transform.localScale;
        newScale.x *= res;
        newScale.y *= res;

        transform.localScale = newScale;
    }

    // Update is called once per frame
    //void Update()
    //{
        
    //}
}
