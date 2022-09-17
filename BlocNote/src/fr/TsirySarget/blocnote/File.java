package fr.TsirySarget.blocnote;

class File
{
  public int id;
  public String name;

  public File(int id, String name)
  {
    this.id=id;
    this.name=name;
  }

  public String toString()
  {
    return "id :"+this.id+" name : "+this.name;
  }
}
