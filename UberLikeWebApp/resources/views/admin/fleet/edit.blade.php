@extends('admin.layout.base')

@section('title', 'Update Fleet ')

@section('content')

<div class="content-area py-1">
    <div class="container-fluid">
    	<div class="box box-block bg-white">
    	    <a href="{{ route('admin.fleet.index') }}" class="btn btn-default pull-right"><i class="fa fa-angle-left"></i> Back</a>

			<h5 style="margin-bottom: 2em;">Update Fleet</h5>

            <form class="form-horizontal" action="{{route('admin.fleet.update', $fleet->id )}}" method="POST" enctype="multipart/form-data" role="form">
            	{{csrf_field()}}
            	<input type="hidden" name="_method" value="PATCH">
				<div class="form-group row">
					<label for="name" class="col-xs-2 col-form-label">Full Name</label>
					<div class="col-xs-10">
						<input class="form-control" type="text" value="{{ $fleet->name }}" name="name" required id="name" placeholder="Full Name">
					</div>
				</div>

				<div class="form-group row">
					<label for="company" class="col-xs-2 col-form-label">Company Name</label>
					<div class="col-xs-10">
						<input class="form-control" type="text" value="{{ $fleet->company }}" name="company" required id="company" placeholder="Company Name">
					</div>
				</div>


				<div class="form-group row">
					
					<label for="logo" class="col-xs-2 col-form-label">Company Logo</label>
					<div class="col-xs-10">
					@if(isset($fleet->logo))
                    	<img style="height: 90px; margin-bottom: 15px; border-radius:2em;" src="{{img($fleet->logo)}}">
                    @endif
						<input type="file" accept="image/*" name="logo" class="dropify form-control-file" id="logo" aria-describedby="fileHelp">
					</div>
				</div>

				<div class="form-group row">
					<label for="mobile" class="col-xs-2 col-form-label">Mobile</label>
					<div class="col-xs-10">
						<input class="form-control" type="number" value="{{ $fleet->mobile }}" name="mobile" required id="mobile" placeholder="Mobile">
					</div>
				</div>

				<div class="form-group row">
					<label for="zipcode" class="col-xs-2 col-form-label"></label>
					<div class="col-xs-10">
						<button type="submit" class="btn btn-primary">Update Fleet Owner</button>
						<a href="{{route('admin.fleet.index')}}" class="btn btn-default">Cancel</a>
					</div>
				</div>
			</form>
		</div>
    </div>
</div>

@endsection
